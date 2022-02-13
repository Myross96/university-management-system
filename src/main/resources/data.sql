INSERT INTO courses VALUES(1, 'biology', 'learning biology');
INSERT INTO courses VALUES(2, 'math', 'learning math');
INSERT INTO courses VALUES(3, 'history', 'learning history');
INSERT INTO courses VALUES(4, 'sience', 'learning sience');
INSERT INTO courses VALUES(5, 'geography', 'learning geography');

INSERT INTO lectures_times VALUES (1, '08:30', '10:05');
INSERT INTO lectures_times VALUES (2, '10:20', '11:55');
INSERT INTO lectures_times VALUES (3, '12:10', '13:45');
INSERT INTO lectures_times VALUES (4, '14:15', '15:50');
INSERT INTO lectures_times VALUES (5, '16:05', '17:40');

INSERT INTO audiences VALUES (1, 124, 45);
INSERT INTO audiences VALUES (2, 234, 95);
INSERT INTO audiences VALUES (3, 244, 105);
INSERT INTO audiences VALUES (4, 133, 60);
INSERT INTO audiences VALUES (5, 115, 55);

INSERT INTO holidays VALUES (1, 'first holiday', '2021-01-01');
INSERT INTO holidays VALUES (2, 'seconf holiday', '2021-01-07');
INSERT INTO holidays VALUES (3, 'thirt holiday', '2021-03-08');
INSERT INTO holidays VALUES (4, 'forth holiday', '2021-06-25');
INSERT INTO holidays VALUES (5, 'fifth holiday', '2021-08-28');

INSERT INTO groups VALUES (1, 'first group');
INSERT INTO groups VALUES (2, 'second group');
INSERT INTO groups VALUES (3, 'thirt group');

INSERT INTO teachers VALUES (1, 'Jennifer', 'Davies', '1973-09-04', 'FEMALE', 'email1', 'address1', 78542, 'BACHELOR', 12);
INSERT INTO teachers VALUES (2, 'James', 'Jones', '1981-06-15', 'FEMALE', 'email2', 'address2', 42318, 'MASTER', 4);
INSERT INTO teachers VALUES (3, 'Karen', 'Davies', '1968-07-23', 'FEMALE', 'email3', 'address4', 57135, 'DOCTOR', 25);

INSERT INTO vacations VALUES (1, '2021-01-17', '2021-02-10', 1);
INSERT INTO vacations VALUES (2, '2021-01-23', '2021-02-16', 2);
INSERT INTO vacations VALUES (3, '2021-03-15', '2021-03-29', 3);
INSERT INTO vacations VALUES (4, '2021-04-17', '2021-05-02', 1);
INSERT INTO vacations VALUES (5, '2021-05-30', '2021-06-17', 2);

INSERT INTO students VALUES (1, 'James', 'Smith', 'MALE', '1995-07-05', 'address1', 'email1', '13256', 1);
INSERT INTO students VALUES (2, 'Jennifer', 'Jones', 'FEMALE', '1996-10-08', 'address2', 'email2', '78542', 2);
INSERT INTO students VALUES (3, 'William', 'Taylor', 'MALE', '1995-03-04', 'address3', 'email3', '54236', 3);
INSERT INTO students VALUES (4, 'Barbara', 'Davies', 'FEMALE', '1995-03-12', 'address4', 'email4', '48236', 1);
INSERT INTO students VALUES (5, 'Karen', 'Brown', 'FEMALE', '1996-12-07', 'address5', 'email5', '01236', 2);

INSERT INTO lectures VALUES (1, 'biology', 1, 1, 1, 1, '2021-03-15');
INSERT INTO lectures VALUES (2, 'math', 2, 2, 2, 2, '2021-03-17');
INSERT INTO lectures VALUES (3, 'history', 3, 3, 3, 3, '2021-03-19');
INSERT INTO lectures VALUES (4, 'biology', 4, 1, 2, 4, '2021-03-15');
INSERT INTO lectures VALUES (5, 'math', 5, 2, 1, 5, '2021-03-17');

INSERT INTO groups_courses VALUES (1, 1);
INSERT INTO groups_courses VALUES (1, 2);
INSERT INTO groups_courses VALUES (2, 3);
INSERT INTO groups_courses VALUES (2, 4);
INSERT INTO groups_courses VALUES (3, 5);
INSERT INTO groups_courses VALUES (3, 1);

INSERT INTO lectures_groups VALUES (1, 1);
INSERT INTO lectures_groups VALUES (1, 3);
INSERT INTO lectures_groups VALUES (2, 1);
INSERT INTO lectures_groups VALUES (3, 2);
INSERT INTO lectures_groups VALUES (4, 2);
INSERT INTO lectures_groups VALUES (5, 3);

INSERT INTO teachers_courses VALUES (3, 5);
INSERT INTO teachers_courses VALUES (3, 3);
INSERT INTO teachers_courses VALUES (2, 1);
INSERT INTO teachers_courses VALUES (2, 4);
INSERT INTO teachers_courses VALUES (1, 2);

INSERT INTO teachers_groups VALUES (3, 3);
INSERT INTO teachers_groups VALUES (3, 2);
INSERT INTO teachers_groups VALUES (2, 1);
INSERT INTO teachers_groups VALUES (2, 3);
INSERT INTO teachers_groups VALUES (2, 2);
INSERT INTO teachers_groups VALUES (1, 1);
