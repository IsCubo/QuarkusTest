package org.acme.domain;

public enum Status {
    PENDING{
        @Override
        public boolean canTransitionTo(Status target) {
            return target == APPROVED || target == REJECTED;
        }
    },
    APPROVED,
    REJECTED;

    public boolean canTransitionTo(Status target) {
        return false;
    }
}
