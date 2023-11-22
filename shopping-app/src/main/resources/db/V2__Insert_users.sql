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
 'user@a.com',
 'user@a.com',
 CURRENT_TIMESTAMP,
 'pass',
 'no_image.png'
);
