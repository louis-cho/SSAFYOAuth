package com.ssafy.authorization.member.model.domain;

public enum MemberRoleEnum {
    USER(Authority.USER),
    DEVELOPER(Authority.DEVELOPER),
    ADMIN(Authority.ADMIN);
    private final String authority;

    MemberRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String DEVELOPER = "ROLE_DEVELOPER";
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}


