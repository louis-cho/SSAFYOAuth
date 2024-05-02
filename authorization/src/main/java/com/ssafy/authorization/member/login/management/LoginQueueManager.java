package com.ssafy.authorization.member.login.management;


import com.ssafy.authorization.member.login.controller.LoginController;
import com.ssafy.authorization.member.login.model.LoginRequest;
import com.ssafy.authorization.member.login.model.PriorityQueueNode;
import lombok.Getter;
import org.apache.tomcat.util.threads.VirtualThreadExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoginQueueManager implements Runnable {
    private final LinkedList<PriorityQueueNode>[] procs;
    private final ConcurrentHashMap<Integer, PriorityQueueNode> teamNodes;
    private ConcurrentHashMap<Integer, AtomicInteger> teamTpsMap;

    private final AuthenticationManager authenticationManager;

    @Getter
    private AtomicInteger queueSize;
    private static final int NUM_PRIORITIES = 100;


    public Authentication authenticateLogin() throws Exception {
        LoginRequest loginRequest = dequeue(); // 로그인 요청을 대기열에서 가져옴
        if (loginRequest == null) {
            return null;
        }
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (AuthenticationException e) {
            return null;
        }
    }

    @Autowired
    public LoginQueueManager(AuthenticationManager authenticationManager) {

        this.procs = new LinkedList[NUM_PRIORITIES];
        this.teamNodes = new ConcurrentHashMap<>();
        this.teamTpsMap = new ConcurrentHashMap<>();
        this.queueSize = new AtomicInteger(0);
        // 우선 순위에 따라 큐를 초기화합니다.
        for (int i = 0; i < NUM_PRIORITIES; i++) {
            this.procs[i] = new LinkedList<>();
        }

        this.authenticationManager = authenticationManager;

        schedulePriorityRestoration();
    }

    public LoginRequest dequeue() {
        for (int i = 0; i < NUM_PRIORITIES; i++) {
            if (!procs[i].isEmpty()) {
                PriorityQueueNode node = procs[i].poll();
                node.setLastAccessTime(LocalDateTime.now());
                LoginRequest request = node.getRequests().poll();
                teamTpsMap.get(node.getTeamId()).getAndIncrement();
                // 우선순위 변경 로직 (예: 우선순위 감소)
                int newPriority = getCurrentPriority(node.getTeamId());
                procs[newPriority].addLast(node);
                return request;
            }
        }
        return null;
    }
    public boolean enqueue(LoginRequest request) {
        int teamId = request.getTeamId();
        PriorityQueueNode node = teamNodes.computeIfAbsent(teamId, k -> new PriorityQueueNode(teamId));

        int currentPriority = getCurrentPriority(teamId);
        procs[currentPriority].addLast(node);
        node.getRequests().add(request);

        AtomicInteger tps = teamTpsMap.getOrDefault(teamId, new AtomicInteger(0));
        tps.incrementAndGet();
        teamTpsMap.put(teamId, tps);
        return true;
    }

    private int getCurrentPriority(int teamId) {
        int currentPriority = 0; // 기본값 설정

        // 팀의 TPS에 따라 현재 우선순위 결정
        // 팀의 TPS가 높을수록 우선순위를 낮게 설정
        AtomicInteger tps = teamTpsMap.getOrDefault(teamId, new AtomicInteger(0));
        int currentTps = tps.get();

        // TPS가 0 이상이면서 일정 수준 이상인 경우 우선순위를 높임
        currentPriority = currentTps / 100 > NUM_PRIORITIES ? NUM_PRIORITIES - 1 : currentTps / 100;

        return currentPriority;
    }

    @Scheduled(fixedRate = 5000)
    private void schedulePriorityRestoration() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        LocalDateTime current = LocalDateTime.now();

        AtomicInteger qSize = new AtomicInteger();
        for (LinkedList<PriorityQueueNode> queue : procs) {
            for (PriorityQueueNode node : queue) {
                qSize.addAndGet(node.getRequests().size());
                if (node.getLastAccessTime() != null && node.getLastAccessTime().plusSeconds(10).isBefore(current)) {
                    restorePriority(node);
                }
            }
        }
        queueSize = qSize;
    }

    private void restorePriority(PriorityQueueNode node) {
        int currentPriority = getCurrentPriority(node.getTeamId());

        int newPriority = Math.max(0, currentPriority / 10); // 우선순위를 낮춤
        teamTpsMap.get(node.getTeamId()).set(newPriority);
        // 현재 우선순위 큐에서 해당 노드를 제거
        for (LinkedList<PriorityQueueNode> queue : procs) {
            if (queue.contains(node)) {
                queue.remove(node);
                break;
            }
        }

        // 새로운 우선순위 큐에 노드를 삽입
        procs[newPriority].addLast(node);
    }

    @Override
    public void run() {
        while(true) {
            if (this.getQueueSize().intValue() > 0) {
                System.out.println(this.authenticateLogin());
            }
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}