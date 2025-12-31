-- 学生データ（student_uuid は書かない。DBが自動で振る）
INSERT INTO students (name, furigana_name, nickname, email, address, age, gender, remark)
VALUES
('Mark Zackerberg', 'マーク ザッカーバーグ', 'The Meta Lord', 'zuck@not_a_robot.com', 'Metaverse', 39, 'Robot?', 'Claims to be human, but blinks suspiciously rarely.'),
('Kim Kardashian', 'キム カーダシアン', 'Break The Internet', 'kim@skims.com', 'Hidden Hills, CA', 43, 'Female', 'Asked if the database could be filtered by "Vibe".'),
('Gordon Ramsay', 'ゴードン ラムゼイ', 'Chef Fury', 'its_raw@hellskitchen.uk', 'London, UK', 57, 'Male', 'Called the SQL syntax "f***ing raw" and threw a spatula.'),
('Rick Sanchez', 'リック サンチェス', 'Pickle Rick', 'wubba_lubba@dub_dub.com', 'C-137', 70, 'Male', 'Turned himself into a pickle to skip the midterm exam.'),
('Taylor Swift', 'テーラー スウィフト', 'The Eras Queen', 'bad_blood@reputation.com', 'New York, NY', 34, 'Female', 'Will write a 10-minute song about this database if it crashes.');

-- コースデータ（uuid は書かない。DBが自動で振る）
-- ただし、外部キー(student_uuid)は「どの学生か」を特定するために必要です
INSERT INTO student_courses (student_uuid, course_name, start_date, end_date)
VALUES
((SELECT student_uuid FROM students WHERE name = 'Mark Zackerberg'), 'Java Course', '2025-01-01 10:00:00', '2026-01-01 10:00:00'),
((SELECT student_uuid FROM students WHERE name = 'Kim Kardashian'), 'Marketing Course', '2025-02-01 10:00:00', '2026-02-01 10:00:00'),
((SELECT student_uuid FROM students WHERE name = 'Gordon Ramsay'), 'Cooking(SQL) Course', '2025-03-01 10:00:00', '2026-03-01 10:00:00'),
((SELECT student_uuid FROM students WHERE name = 'Rick Sanchez'), 'Interdimensional Science', '2025-04-01 10:00:00', '2026-04-01 10:00:00'),
((SELECT student_uuid FROM students WHERE name = 'Taylor Swift'), 'Songwriting Course', '2025-05-01 10:00:00', '2026-05-01 10:00:00');