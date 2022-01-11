use `External`;

-- set @adminRole = 1;
-- set @artistRole = 2;
-- set @userRole = 3;

create table `user`(
	`id` int auto_increment	primary key,
	`display_name` tinytext not null,
	`email` tinytext not null unique,
	`password` tinytext not null,
	`avatar_path` text,
	`role` tinyint not null,
	`status` bool not null default 1,
);	

create table `album`(
	`id` int auto_increment primary key,
	`title` tinytext not null,
	`artist_id` int not null,
	`release_date` date not null,
	`type` int not null,
	foreign key (`artist_id`) references `user`(`id`)	
);

create table `song`(
	`id` int auto_increment primary key,
	`album_id` int not null,
	`track_num` int not null,
	`track_name` tinytext not null,
	foreign key (`album_id`) references `album`(`id`)
);

create table `playlist`(
	`id` int auto_increment primary key,
	`creator_id` int not null,
	`visibility` int not null,	
	foreign key (`creator_id`) references `user`(`id`)
);

create table `playlist_song`(
	`playlist_id` int not null,
	`song_id` int not null,
	`added_date` date not null default current_date(),
	foreign key (`playlist_id`) references `playlist`(`id`)
	foreign key (`song_id`) references `song`(`id`)
);

create table `comment`(
	`id` int auto_increment primary key,
	`playlist_id` int,
	`album_id` int,
	`creator_id` int not null,
	`created_timestamp` timestamp not null default current_timestamp(),
	`content` text not null,
	foreign key (`creator_id`) references `user`(`id`),
	foreign key (`album_id`) references `album`(`id`),
	foreign key (`playlist_id`) references `playlist`(`id`)
);