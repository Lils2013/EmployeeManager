INSERT INTO EMPLOYEE_REVINFO (ID, TIMESTAMP) VALUES (EMPLOYEE_REV_SEQUENCE.nextval, 0);
INSERT INTO DEPARTMENT (ID, IS_DISMISSED, NAME) VALUES (DEPARTMENT_SEQUENCE.nextval, 0, 'ADMIN');
INSERT INTO DEPARTMENT_AUD (ID, REV, REVTYPE, IS_DISMISSED, NAME) VALUES (DEPARTMENT_SEQUENCE.currval, 1, 0, 0, 'ADMIN');
INSERT INTO POSITION (ID, NAME) VALUES (POSITION_SEQUENCE.nextval, 'Administrator');
INSERT INTO POSITION_AUD (ID, REV, REVTYPE, NAME) VALUES (POSITION_SEQUENCE.currval, EMPLOYEE_REV_SEQUENCE.currval, 0, 'Administrator');
INSERT INTO GRADE (ID, GRADE) VALUES (GRADE_SEQUENCE.nextval, 'A1');
INSERT INTO GRADE_AUD (ID, REV, REVTYPE, GRADE) VALUES (GRADE_SEQUENCE.currval, EMPLOYEE_REV_SEQUENCE.currval, 0, 'A1');
INSERT INTO EMPLOYEE (ID, IS_FIRED, PASSWORD, USERNAME, FIRSTNAME, LASTNAME, DEPARTMENT_ID, POSITION_ID, GRADE_ID) VALUES (EMPLOYEE_SEQUENCE.nextval, 0, '$2a$06$wZqzsXQFiu5Z21bezTnGWOQg55AH1ojWp6XeQ.050zLjevfElm3Yy', 'admin', 'JON', 'SAND', 1, 1, 1);
INSERT INTO EMPLOYEE_AUD (ID, REV, REVTYPE, IS_FIRED, PASSWORD, USERNAME, FIRSTNAME, LASTNAME, DEPARTMENT_ID, POSITION_ID, GRADE_ID) VALUES (EMPLOYEE_SEQUENCE.currval, 1, 0, 0, '$2a$06$wZqzsXQFiu5Z21bezTnGWOQg55AH1ojWp6XeQ.050zLjevfElm3Yy', 'admin', 'JON', 'SAND', 1, 1, 1);
INSERT INTO ROLES_LIST (ROLE_ID, EMPLOYEE_ID) VALUES ('ROLE_ADMIN', EMPLOYEE_REV_SEQUENCE.currval);
INSERT INTO ROLES_LIST_AUD (REV, EMPLOYEE_ID, ROLE_ID, REVTYPE) VALUES (EMPLOYEE_REV_SEQUENCE.currval, EMPLOYEE_SEQUENCE.currval, 'ROLE_ADMIN', 0);
INSERT INTO ROLES_LIST (ROLE_ID, EMPLOYEE_ID) VALUES ('ROLE_USER', EMPLOYEE_REV_SEQUENCE.currval);
INSERT INTO ROLES_LIST_AUD (REV, EMPLOYEE_ID, ROLE_ID, REVTYPE) VALUES (EMPLOYEE_REV_SEQUENCE.currval, EMPLOYEE_SEQUENCE.currval, 'ROLE_USER', 0);
INSERT INTO ROLES_LIST (ROLE_ID, EMPLOYEE_ID) VALUES ('ROLE_EDITOR', EMPLOYEE_REV_SEQUENCE.currval);
INSERT INTO ROLES_LIST_AUD (REV, EMPLOYEE_ID, ROLE_ID, REVTYPE) VALUES (EMPLOYEE_REV_SEQUENCE.currval, EMPLOYEE_SEQUENCE.currval, 'ROLE_EDITOR', 0);

