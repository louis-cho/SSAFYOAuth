package com.ssafy.authorization.member.login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Queue;
import java.util.ArrayDeque;

@AllArgsConstructor
@Getter
@Setter
public class PriorityQueueNode {
    private int teamId;
    private Queue<LoginRequest> requests;
    private PriorityQueueNode prev;
    private PriorityQueueNode next;
    private LocalDateTime lastAccessTime;

    public PriorityQueueNode(int teamId) {
        this.teamId = teamId;
        this.requests = new ArrayDeque<>();
        this.prev = null;
        this.next = null;
        this.lastAccessTime = null;
    }


}
