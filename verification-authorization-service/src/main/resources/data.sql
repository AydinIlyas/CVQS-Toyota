INSERT INTO "ToyotaProject".public.roles (name,description) VALUES
                                         ('ADMIN','ADMIN'),
                                         ('LEADER','LEADER'),
                                         ('OPERATOR','OPERATOR');

INSERT INTO "ToyotaProject".public.permission (name,description) VALUES
    ('TEST','ADMIN'),
    ('TEST1','LEADER'),
    ('TEST2','OPERATOR');

INSERT INTO "ToyotaProject".public.roles_permissions (role_id,permission_id) VALUES
                                                          (1,1),
(2,2),
(3,3);
INSERT INTO "ToyotaProject".public.users_auth (username,deleted) VALUES
                                                                      ('admin',false)
INSERT INTO "ToyotaProject".public.users_roles (role_id,user_id) VALUES
    (1,2)