CREATE TABLE notification_task
(
    id             BIGINT PRIMARY KEY,
    chat_id        VARCHAR(255)  NOT NULL,
    task_text      VARCHAR(1000) NOT NULL,
    scheduled_time TIMESTAMP     NOT NULL
);