package com.personal.community.common;

import lombok.AllArgsConstructor;

public class CommunityEnum {

    @AllArgsConstructor
    public enum PostType implements EnumType{
        PROJECT("프로젝트"),
        NOTICE("공지"),
        FREE_BOARD("자유 게시판");

        private final String description;

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }

    @AllArgsConstructor
    public enum UserRole implements EnumType{
        ADMIN("관리자"),
        USER("유저");

        private String description;

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }

    @AllArgsConstructor
    public enum CommentStatus implements EnumType{
        EXISTS("보임"),
        DELETED("삭제됨");

        private String description;

        @Override
        public String getName() {
            return this.name();
        }

        @Override
        public String getDescription() {
            return this.description;
        }
    }
}
