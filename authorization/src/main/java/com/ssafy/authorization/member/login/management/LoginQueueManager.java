package com.ssafy.authorization.member.login.management;


import com.ssafy.authorization.member.login.controller.LoginController;
import com.ssafy.authorization.member.login.model.LoginRequest;
import com.ssafy.authorization.member.login.model.PriorityQueueNode;
import lombok.Getter;
import org.apache.tomcat.util.threads.VirtualThreadExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LoginQueueManager implements Runnable {
    private final ConcurrentLinkedQueue<PriorityQueueNode>[] procs;
    private final ConcurrentHashMap<Integer, PriorityQueueNode> teamNodes;
    private ConcurrentHashMap<Integer, AtomicInteger> teamTpsMap;

    private final LoginController loginController;
    @Getter
    private AtomicInteger queueSize;
    private static final int NUM_PRIORITIES = 100;


    @Autowired
    public LoginQueueManager(@Lazy LoginController loginController) {

        this.procs = (ConcurrentLinkedQueue<PriorityQueueNode>[]) new ConcurrentLinkedQueue[NUM_PRIORITIES];
        this.teamNodes = new ConcurrentHashMap<>();
        this.teamTpsMap = new ConcurrentHashMap<>();
        this.queueSize = new AtomicInteger(0);
        // 우선 순위에 따라 큐를 초기화합니다.
        for (int i = 0; i < NUM_PRIORITIES; i++) {
            this.procs[i] = new ConcurrentLinkedQueue<>();
        }

        this.loginController = loginController;

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
                    if(node.getRequests().size() > 0) {
                        int newPriority = getCurrentPriority(node.getTeamId());
                        procs[newPriority].add(node);
                    }

                    return request;
                }

        }
        return null;
    }

    public LoginRequest processLoginRequest() {
        LoginRequest request = dequeue(); // 큐에서 요청을 하나 가져옵니다.
        if(request != null) {
            loginController.notifyLoginResult(request);
        }
        return request;
    }

    public boolean enqueue(LoginRequest request) {

            int teamId = request.getTeamId();
            PriorityQueueNode node = teamNodes.computeIfAbsent(teamId, k -> new PriorityQueueNode(teamId));

            int currentPriority = getCurrentPriority(teamId);
            procs[currentPriority].add(node);

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
        LocalDateTime current = LocalDateTime.now();
        AtomicInteger qSize = new AtomicInteger();

        for (ConcurrentLinkedQueue<PriorityQueueNode> queue : procs) {
            Iterator<PriorityQueueNode> iterator = queue.iterator();
            while (iterator.hasNext()) {
                PriorityQueueNode node = iterator.next();
                qSize.addAndGet(node.getRequests().size());
                if (node.getLastAccessTime() != null && node.getLastAccessTime().plusSeconds(10).isBefore(current)) {
                    restorePriority(node, iterator); // restorePriority에 iterator를 전달하여 제거 수행
                }
            }
        }
        queueSize = qSize;
    }

    private void restorePriority(PriorityQueueNode node, Iterator<PriorityQueueNode> iterator) {
        int currentPriority = getCurrentPriority(node.getTeamId());
        int newPriority = Math.max(0, currentPriority / 10);
        teamTpsMap.get(node.getTeamId()).set(newPriority);

        iterator.remove(); // 안전하게 현재 요소를 제거
        procs[newPriority].add(node); // 새 우선순위에 노드를 추가
    }

    @Override
    public void run() {
        while(true) {
            // Queue size를 확인하여 요청이 있는 경우에만 로그인 처리
            if (this.getQueueSize().get() > 0) {
                LoginRequest request = this.processLoginRequest();
                if (request != null) {
                    System.out.println("Processed login request: " + request);
                }
            }
            try {
                Thread.sleep(1); // 큐가 비어있는 경우 CPU 사용을 줄이기 위해 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 인터럽트 발생 시 스레드 인터럽트 상태를 복구
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }
    }
}