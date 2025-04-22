CREATE TABLE files(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR NOT NULL,
    size BIGINT NOT NULL,
    create_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE TABLE departments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    established_date TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE employee_status AS ENUM('ACTIVE', 'ON_LEAVE', 'RESIGNED');

CREATE TABLE employees(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    employee_number VARCHAR(50) NOT NULL UNIQUE,
    department_id BIGINT NOT NULL,
    position VARCHAR(100) NOT NULL,
    hire_date TIMESTAMPTZ NOT NULL,
    status employee_status DEFAULT 'ACTIVE',
    profile_image_id BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_employees_profile_image_id
        FOREIGN KEY (profile_image_id)
            REFERENCES files(id)
            ON DELETE SET NULL,

    CONSTRAINT fk_employees_department_id
        FOREIGN KEY (department_id)
            REFERENCES departments(id)
            ON DELETE RESTRICT
);

CREATE TABLE employee_change_logs (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(100) NOT NULL,
    employee_number VARCHAR(50) NOT NULL,
    employee_diffs JSONB,
    memo TEXT,
    ip_address VARCHAR(50) NOT NULL,
    at TIMESTAMPTZ NOT NULL
);

CREATE TYPE backup_status AS ENUM ('IN_PROGRESS', 'COMPLETED', 'SKIPPED', 'FAILED');
CREATE TABLE backups (
    id BIGSERIAL PRIMARY KEY,
    worker VARCHAR(50) NOT NULL,
    started_at TIMESTAMPTZ NOT NULL,
    ended_at TIMESTAMPTZ NOT NULL,
    status backup_status,
    file_id BIGINT,
    CONSTRAINT fk_file
        FOREIGN KEY (file_id)
            REFERENCES files(id)
            ON DELETE SET NULL
);
