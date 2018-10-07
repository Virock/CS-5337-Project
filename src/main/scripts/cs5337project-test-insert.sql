INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(111),
(111),
(111),
(111),
(111),
(111),
(111),
(111),
(111),
(111),
(111),
(111),
(111),
(111);

INSERT INTO `users` (`id`, `cin`, `email`, `enabled`, `name`, `password`, `token`, `type`) VALUES
(1, 123456789, 'user1@email.com', b'1', 'User1', 'password', 'i', 1),
(2, 12345, 'admin@admin.com', b'1', 'Admin', 'password', 'a', 0),
(3, 123, 'user@user.com', b'1', 'User', 'password', 'r', 2),
(100, 1234567812111111, 'this_should__work22@email.com', b'1', 'Victor', 'password', '1538451325787Y0hw0KQAo', 2),
(101, 12345678121112111, 'this_should_also_work22@email.com', b'1', 'Victor', 'password', '1538463303888t86xl', 2),
(108, 123456781211121111, 'this_should_also_work223@email.com', b'1', 'New name', 'password', 'r1', 2);

INSERT INTO `terms` (`id`, `description`) VALUES
(1, 'Fall Semester 2018'),
(103, 'Fall semester 2020'),
(104, 'New description'),
(110, 'New description');

INSERT INTO `classes` (`class_no`,  `description`) VALUES
(4660, 'Artificial Intelligence'),
(4661, 'Data Science');

INSERT INTO `specific_classes` (`id`, `class_end_date`, `class_start_date`, `end_time`, `room`, `section`, `start_time`, `instructor_id`, `class_no`, `term_id`) VALUES
(1, '2018-12-31 00:00:00.000000', '2018-09-01 00:00:00.000000', '2018-10-06 12:00:00.000000', 'ET 401', 1, '2018-10-06 10:00:00.000000', 1, 4660, 1),
(106, '2018-12-31 00:00:00.000000', '2018-09-01 00:00:00.000000', '2018-12-31 00:00:00.000000', 'ET 104', 2, '2018-09-01 00:00:00.000000', 1, 4661, 1);

INSERT INTO `assignments` (`id`, `answer`, `due_date`, `post_date`, `question`, `specific_class_id`) VALUES
(1, '5', '2018-10-04 00:00:00.000000', '2018-09-30 00:00:00.000000', 'Hello World', 1),
(109, '50', '2018-12-31 00:00:00.000000', '2018-09-01 00:00:00.000000', 'Compute 10 * 5', 106);

INSERT INTO `user_assignments` (`id`, `code`, `comment`, `language`, `score`, `assignment_id`, `user_id`) VALUES
(1, 'cout<<\"Hello World\"', NULL, 'Java', 100, 1, 3),
(102, 'cout<<\"Hello World\"', NULL, 'C++', 100, 1, 100);

INSERT INTO `user_classes` (`specific_class_id`, `user_id`) VALUES
(1, 3),
(1, 100);