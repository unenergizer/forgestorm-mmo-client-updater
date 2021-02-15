package com.forgestorm.client.updater;

public enum ProgressState {
    REQUEST_INFORMATION {
        @Override
        public ProgressState nextState() {
            return VERIFY_FILES;
        }
    },
    VERIFY_FILES {
        @Override
        public ProgressState nextState() {
            return DOWNLOAD_FILES;
        }
    },
    DOWNLOAD_FILES {
        @Override
        public ProgressState nextState() {
            return FINISH_UPDATE;
        }
    },
    FINISH_UPDATE {
        @Override
        public ProgressState nextState() {
            return this;
        }
    },
    ERROR {
        @Override
        public ProgressState nextState() {
            return this;
        }
    };

    public abstract ProgressState nextState();

    @Override
    public String toString() {
        String name = name().toLowerCase().replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
