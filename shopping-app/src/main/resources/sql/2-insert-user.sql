INSERT INTO admin_users (
 name,
 email,
 password ,
 is_owner
) VALUES (
 'admin@a.com',
 'admin@a.com',
 'pass',
 1
);

INSERT INTO users (
 name,
 email,
 email_verified_at,
 password,
 image_path
) VALUES (
 'user1@a.com',
 'user1@a.com',
 CURRENT_TIMESTAMP,
 'pass',
 'no_image.png'
);

INSERT INTO users (
 name,
 email,
 email_verified_at,
 password,
 image_path
) VALUES (
 'user2@a.com',
 'user2@a.com',
 CURRENT_TIMESTAMP,
 'pass',
 'no_image.png'
);

INSERT INTO users (
 name,
 email,
 email_verified_at,
 password,
 image_path
) VALUES (
 'user3@a.com',
 'user3@a.com',
 CURRENT_TIMESTAMP,
 'pass',
 'no_image.png'
);
