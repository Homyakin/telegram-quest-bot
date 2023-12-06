create table user_current_quest(
	user_id numeric primary key,
	quest_name varchar(100),
	quest_stage varchar(100)
);

create table user2answers(
	user_id numeric,
	quest_name varchar(100),
	quest_stage varchar(100),
	user_answer text,
	date_insert timestamp default current_timestamp
);

create table quests(
	id serial,
	name varchar(100) primary key,
	description varchar(100),
	available bool,
	start_stage_id numeric
);

create table quest_stages(
	id serial,
	quest_name varchar(100),
	name varchar(100),
	description text,
	photo_path varchar(200),
	constraint qs_uq unique(quest_name, name)
);

create table available_answers(
	id serial,
	name varchar(100) primary key,
	type varchar(50),
	check_value varchar(1000),
	next_stage_id numeric
);

create table stage2answers(
	stage_id numeric,
	answer_id numeric,
	constraint s2a_uq unique(stage_id, answer_id)
);
