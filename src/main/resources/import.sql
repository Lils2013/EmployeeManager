INSERT INTO TEST_B.USERS (ID, ENABLED, PASSWORD, USERNAME) VALUES (1, 1, '$2a$06$wZqzsXQFiu5Z21bezTnGWOQg55AH1ojWp6XeQ.050zLjevfElm3Yy', 'admin');
INSERT INTO TEST_B.ROLE (ID, NAME) VALUES (1, 'ROLE_ADMIN');
INSERT INTO TEST_B.ROLE (ID, NAME) VALUES (2, 'ROLE_EDITOR');
INSERT INTO TEST_B.ROLE (ID, NAME) VALUES (3, 'ROLE_USER');
INSERT INTO TEST_B.ROLES_LIST (ID, ROLE_ID, USER_ID) VALUES (1, 1, 1);
INSERT INTO TEST_B.ROLES_LIST (ID, ROLE_ID, USER_ID) VALUES (2, 2, 1);
INSERT INTO TEST_B.ROLES_LIST (ID, ROLE_ID, USER_ID) VALUES (3, 3, 1);
