create table assignments (
       id bigint not null,
        answer varchar(255) not null,
        due_date datetime(6) not null,
        post_date datetime(6) not null,
        question varchar(255) not null,
        specific_class_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table classes (
       class_no bigint not null,
        description varchar(255) not null,
        primary key (class_no)
    ) engine=InnoDB;

    create table hibernate_sequence (
       next_val bigint
    ) engine=InnoDB;

    insert into hibernate_sequence values ( 111 );

    insert into hibernate_sequence values ( 111 );

    insert into hibernate_sequence values ( 111 );

    insert into hibernate_sequence values ( 111 );

    insert into hibernate_sequence values ( 111 );

    create table specific_classes (
       id bigint not null,
        class_end_date datetime(6) not null,
        class_start_date datetime(6) not null,
        end_time datetime(6) not null,
        room varchar(255) not null,
        section bigint not null,
        start_time datetime(6) not null,
        instructor_id bigint not null,
        class_no bigint not null,
        term_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table terms (
       id bigint not null,
        description varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table user_assignments (
       id bigint not null,
        code varchar(255),
        comment varchar(255),
        language varchar(255),
        score bigint not null,
        assignment_id bigint not null,
        user_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table user_classes (
       specific_class_id bigint not null,
        user_id bigint not null
    ) engine=InnoDB;

    create table users (
       id bigint not null,
        cin bigint not null,
        email varchar(255) not null,
        enabled bit not null,
        name varchar(255) not null,
        password varchar(255) not null,
        token varchar(255) not null,
        type integer not null,
        primary key (id)
    ) engine=InnoDB;

    alter table users 
       add constraint UK_ka6m8ghsr7vna1ti6lftwww8o unique (cin);

    alter table users 
       add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UK_af44yue4uh61eqwe6jjyqwvla unique (token);

    alter table assignments 
       add constraint FKk3owmj5x5qmlmo0qkea9sggi7 
       foreign key (specific_class_id) 
       references specific_classes (id);

    alter table specific_classes 
       add constraint FKmknlncdsfgd346ugbdloxxb43 
       foreign key (instructor_id) 
       references users (id);

    alter table specific_classes 
       add constraint FKipuevgfobdj9obp7qia2f9q8w 
       foreign key (class_no) 
       references classes (class_no);

    alter table specific_classes 
       add constraint FK3lr9f3h9itrvef8o2k4chhksa 
       foreign key (term_id) 
       references terms (id);

    alter table user_assignments 
       add constraint FKr5uhx1ygcongw03w13208mfej 
       foreign key (assignment_id) 
       references assignments (id);

    alter table user_assignments 
       add constraint FKjg6ivf85bd67g8bhm4uwv1ph8 
       foreign key (user_id) 
       references users (id);

    alter table user_classes 
       add constraint FKqen46xej8c5lyl9hwmxnoq4s0 
       foreign key (user_id) 
       references users (id);

    alter table user_classes 
       add constraint FKkq4l6mr4m3s9dt58xtlfw21ys 
       foreign key (specific_class_id) 
       references specific_classes (id);