DROP DATABASE  IF EXISTS jobportal;
CREATE DATABASE `jobportal`;
USE `jobportal`;

CREATE TABLE `users_type` (
  `user_type_id` int NOT NULL AUTO_INCREMENT,
  `user_type_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `users_type` VALUES (1,'Recruiter'),(2,'Job Seeker');


CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `registration_date` datetime(6) DEFAULT NULL,
  `user_type_id` int DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FK5snet2ikvi03wd4rabd40ckdl` (`user_type_id`),
  CONSTRAINT `FK5snet2ikvi03wd4rabd40ckdl` FOREIGN KEY (`user_type_id`) REFERENCES `users_type` (`user_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `job_company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logo` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `job_location` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `job_seeker_profile` (
  `user_account_id` int NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `employment_type` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `profile_photo` varchar(255) DEFAULT NULL,
  `resume` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `work_authorization` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_account_id`),
  CONSTRAINT `FKohp1poe14xlw56yxbwu2tpdm7` FOREIGN KEY (`user_account_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `recruiter_profile` (
  `user_account_id` int NOT NULL,
  `city` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `profile_photo` varchar(64) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_account_id`),
  CONSTRAINT `FK42q4eb7jw1bvw3oy83vc05ft6` FOREIGN KEY (`user_account_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `job_post_activity` (
  `job_post_id` int NOT NULL AUTO_INCREMENT,
  `description_of_job` varchar(10000) DEFAULT NULL,
  `job_title` varchar(255) DEFAULT NULL,
  `job_type` varchar(255) DEFAULT NULL,
  `posted_date` datetime(6) DEFAULT NULL,
  `remote` varchar(255) DEFAULT NULL,
  `salary` varchar(255) DEFAULT NULL,
  `job_company_id` int DEFAULT NULL,
  `job_location_id` int DEFAULT NULL,
  `posted_by_id` int DEFAULT NULL,
  PRIMARY KEY (`job_post_id`),
  KEY `FKpjpv059hollr4tk92ms09s6is` (`job_company_id`),
  KEY `FK44003mnvj29aiijhsc6ftsgxe` (`job_location_id`),
  KEY `FK62yqqbypsq2ik34ngtlw4m9k3` (`posted_by_id`),
  CONSTRAINT `FK44003mnvj29aiijhsc6ftsgxe` FOREIGN KEY (`job_location_id`) REFERENCES `job_location` (`id`),
  CONSTRAINT `FK62yqqbypsq2ik34ngtlw4m9k3` FOREIGN KEY (`posted_by_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKpjpv059hollr4tk92ms09s6is` FOREIGN KEY (`job_company_id`) REFERENCES `job_company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `job_seeker_save` (
  `id` int NOT NULL AUTO_INCREMENT,
  `job` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1vn1w4dxfiavb5q2gu1n0whxo` (`user_id`,`job`),
  KEY `FKpb44x040gkdltxqy9m7jmvvf3` (`job`),
  CONSTRAINT `FK96dyvgd8hmdohqsfdpvyl89mg` FOREIGN KEY (`user_id`) REFERENCES `job_seeker_profile` (`user_account_id`),
  CONSTRAINT `FKpb44x040gkdltxqy9m7jmvvf3` FOREIGN KEY (`job`) REFERENCES `job_post_activity` (`job_post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `job_seeker_apply` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apply_date` datetime(6) DEFAULT NULL,
  `cover_letter` varchar(255) DEFAULT NULL,
  `job` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8v6qok40anljlhpkc486nsdmu` (`user_id`,`job`),
  KEY `FKmfhx9q4uclbb74vm49lv9dmf4` (`job`),
  CONSTRAINT `FKmfhx9q4uclbb74vm49lv9dmf4` FOREIGN KEY (`job`) REFERENCES `job_post_activity` (`job_post_id`),
  CONSTRAINT `FKs9fftlyxws2ak05q053vi57qv` FOREIGN KEY (`user_id`) REFERENCES `job_seeker_profile` (`user_account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `skills` (
  `id` int NOT NULL AUTO_INCREMENT,
  `experience_level` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `years_of_experience` varchar(255) DEFAULT NULL,
  `job_seeker_profile` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsjdksau8sat30c00aqh5xf2wh` (`job_seeker_profile`),
  CONSTRAINT `FKsjdksau8sat30c00aqh5xf2wh` FOREIGN KEY (`job_seeker_profile`) REFERENCES `job_seeker_profile` (`user_account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;












------------------------------------------------------------------------------------------------------------------------------------------------------------------------


















-- Create users_type table
CREATE TABLE users_type (
    user_type_id SERIAL PRIMARY KEY,
    user_type_name VARCHAR(255)
);

-- Insert values into users_type
INSERT INTO users_type (user_type_name) VALUES
('Recruiter'),
('Job Seeker');

-- Create users table
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    is_active BOOLEAN,
    password VARCHAR(255),
    registration_date TIMESTAMP(6),
    user_type_id INT,
    CONSTRAINT fk_user_type FOREIGN KEY (user_type_id) REFERENCES users_type (user_type_id)
);

-- Create job_company table
CREATE TABLE job_company (
    id SERIAL PRIMARY KEY,
    logo VARCHAR(255),
    name VARCHAR(255)
);

-- Create job_location table
CREATE TABLE job_location (
    id SERIAL PRIMARY KEY,
    city VARCHAR(255),
    country VARCHAR(255),
    state VARCHAR(255)
);

-- Create job_seeker_profile table
CREATE TABLE job_seeker_profile (
    user_account_id INT PRIMARY KEY,
    city VARCHAR(255),
    country VARCHAR(255),
    employment_type VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    profile_photo VARCHAR(255),
    resume VARCHAR(255),
    state VARCHAR(255),
    work_authorization VARCHAR(255),
    CONSTRAINT fk_user_account_id FOREIGN KEY (user_account_id) REFERENCES users (user_id)
);

-- Create recruiter_profile table
CREATE TABLE recruiter_profile (
    user_account_id INT PRIMARY KEY,
    city VARCHAR(255),
    company VARCHAR(255),
    country VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    profile_photo VARCHAR(255),
    state VARCHAR(255),
    CONSTRAINT fk_recruiter_account_id FOREIGN KEY (user_account_id) REFERENCES users (user_id)
);

-- Create job_post_activity table
CREATE TABLE job_post_activity (
    job_post_id SERIAL PRIMARY KEY,
    description_of_job VARCHAR(10000),
    job_title VARCHAR(255),
    job_type VARCHAR(255),
    posted_date TIMESTAMP(6),
    remote VARCHAR(255),
    salary VARCHAR(255),
    job_company_id INT,
    job_location_id INT,
    posted_by_id INT,
    CONSTRAINT fk_job_company_id FOREIGN KEY (job_company_id) REFERENCES job_company (id),
    CONSTRAINT fk_job_location_id FOREIGN KEY (job_location_id) REFERENCES job_location (id),
    CONSTRAINT fk_posted_by_id FOREIGN KEY (posted_by_id) REFERENCES users (user_id)
);

-- Create job_seeker_save table
CREATE TABLE job_seeker_save (
    id SERIAL PRIMARY KEY,
    job INT,
    user_id INT,
    CONSTRAINT fk_job FOREIGN KEY (job) REFERENCES job_post_activity (job_post_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES job_seeker_profile (user_account_id),
    UNIQUE (user_id, job)
);

-- Create job_seeker_apply table
CREATE TABLE job_seeker_apply (
    id SERIAL PRIMARY KEY,
    apply_date TIMESTAMP(6),
    cover_letter VARCHAR(255),
    job INT,
    user_id INT,
    CONSTRAINT fk_job_apply FOREIGN KEY (job) REFERENCES job_post_activity (job_post_id),
    CONSTRAINT fk_user_apply FOREIGN KEY (user_id) REFERENCES job_seeker_profile (user_account_id),
    UNIQUE (user_id, job)
);

-- Create skills table
CREATE TABLE skills (
    id SERIAL PRIMARY KEY,
    experience_level VARCHAR(255),
    name VARCHAR(255),
    years_of_experience VARCHAR(255),
    job_seeker_profile INT,
    CONSTRAINT fk_skills_profile FOREIGN KEY (job_seeker_profile) REFERENCES job_seeker_profile (user_account_id)
);










TRUNCATE TABLE job_seeker_save, job_seeker_apply, skills, job_post_activity, recruiter_profile, job_seeker_profile, 
job_location, job_company, users RESTART IDENTITY CASCADE;

TRUNCATE TABLE job_seeker_apply RESTART IDENTITY CASCADE




SELECT * FROM users;
SELECT * FROM users_type;
SELECT * FROM job_post_activity;
SELECT * FROM job_location;
SELECT * FROM job_company;
SELECT * FROM recruiter_profile;
SELECT * FROM job_seeker_profile;
SELECT * FROM job_seeker_apply;
SELECT * FROM job_seeker_save;
SELECT * FROM skills;






SELECT * FROM users u join users_type t on u.user_type_id = t.user_type_id









