CREATE DATABASE IF NOT EXISTS pdsa_queens;
USE pdsa_queens;

CREATE TABLE solutions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    placement JSON NOT NULL,
    placement_hash VARCHAR(64) UNIQUE NOT NULL,
    is_claimed BOOLEAN DEFAULT FALSE,
    claimed_by VARCHAR(100),
    claimed_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE algorithm_runs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    algorithm_type ENUM('SEQUENTIAL', 'THREADED') NOT NULL,
    started_at DATETIME NOT NULL,
    ended_at DATETIME NOT NULL,
    time_taken_ms BIGINT NOT NULL,
    total_solutions_found INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE game_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_name VARCHAR(100) NOT NULL,
    submitted_placement JSON NOT NULL,
    is_correct BOOLEAN NOT NULL,
    result ENUM('WIN', 'LOSE', 'DRAW') NOT NULL,
    solution_id BIGINT,
    played_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (solution_id) REFERENCES solutions(id)
);