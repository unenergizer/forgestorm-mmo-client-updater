package com.forgestorm.client.updater;

public enum ProgressState {
    REQUESTING_INFORMATION {
        @Override
        public ProgressState nextState() {
            return PARSING_FILE_TREE;
        }
    },
    PARSING_FILE_TREE {
        @Override
        public ProgressState nextState() {
            return CHECKING_FILE_HASH;
        }
    },
    CHECKING_FILE_HASH {
        @Override
        public ProgressState nextState() {
            return PREPARING_DOWNLOAD_STATE;
        }
    },
    PREPARING_DOWNLOAD_STATE {
        @Override
        public ProgressState nextState() {
            return DOWNLOADING_FILES;
        }
    },
    DOWNLOADING_FILES {
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
