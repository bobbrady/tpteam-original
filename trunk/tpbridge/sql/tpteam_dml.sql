/********************************************************************
 * 
 * File		:	tpteam_dml.sql
 *
 * Author	:	Bob Brady, rpbrady@gmail.com
 * 
 * Contents	:	DML statements for inital TPTeam seed data
 * 
 ********************************************************************/
USE TPTEAM;

/* Initial Roles, used by web interface */

INSERT INTO ROLE (ROLE_ID, NAME, DESCRIPTION) 
	VALUES (1, 'admin', 'Admin role');

INSERT INTO ROLE (ROLE_ID, NAME, DESCRIPTION) 
	VALUES (2, 'user', 'Basic User role');

/* Initial administrative user, for web interface 
   Note: Passwords are SHA1 hashes */
   
/* Inital password is "tpteam" for admin user tpteam1 */

INSERT INTO TPTEAM_USER (ID, USER_NAME, PASSWORD, ROLE_ID) 
	VALUES (1, 'tpteam1',  '84FE0D304EE0B3D09C860FA4544E17C5D94D29E0',1);

/* Mandatory test types used by TPTeam */

INSERT INTO TEST_TYPE (ID, NAME)
	VALUES (1, 'Folder');

INSERT INTO TEST_TYPE (ID, NAME)
	VALUES (2, 'JUnit');
