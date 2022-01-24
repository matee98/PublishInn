DROP TABLE IF EXISTS app_user CASCADE;
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS confirmation_token CASCADE;
DROP TABLE IF EXISTS one_time_code CASCADE;
DROP TABLE IF EXISTS rating CASCADE;
DROP TABLE IF EXISTS work CASCADE;

DROP SEQUENCE IF EXISTS code_sequence;
DROP SEQUENCE IF EXISTS comment_sequence;
DROP SEQUENCE IF EXISTS rating_sequence;
DROP SEQUENCE IF EXISTS token_sequence;
DROP SEQUENCE IF EXISTS user_sequence;
DROP SEQUENCE IF EXISTS work_sequence;

CREATE SEQUENCE code_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE comment_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE rating_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE token_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE user_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE work_sequence START WITH 1 INCREMENT BY 1;

create table app_user (
      id bigint not null,
      created_by bigint,
      created_on timestamp not null,
      modified_by bigint,
      modified_on timestamp,
      version bigint,
      app_user_role varchar(255),
      email varchar(127),
      enabled boolean not null,
      locked boolean not null,
      password varchar(255),
      username varchar(19),
      primary key (id)
);

create table comment (
     id bigint not null,
     created_by bigint,
     created_on timestamp not null,
     modified_by bigint,
     modified_on timestamp,
     version bigint,
     text varchar(255),
     user_id bigint,
     visible boolean not null,
     work_id bigint,
     primary key (id)
);

create table confirmation_token (
    id bigint not null,
    confirmed_at timestamp,
    created_at timestamp not null,
    expires_at timestamp not null,
    token varchar(255) not null,
    app_user_id bigint not null,
    primary key (id)
);

create table one_time_code (
   id bigint not null,
   code varchar(255) not null,
   app_user_id bigint not null,
   primary key (id)
);

create table rating (
    id bigint not null,
    created_by bigint,
    created_on timestamp not null,
    modified_by bigint,
    modified_on timestamp,
    version bigint,
    rate smallint not null check (rate>=1 AND rate<=10),
    user_id bigint,
    work_id bigint,
    primary key (id)
);

create table work (
  id bigint not null,
  created_by bigint,
  created_on timestamp not null,
  modified_by bigint,
  modified_on timestamp,
  version bigint,
  rating decimal(3,2) check (rating>=1 AND rating<=10),
  status varchar(255) not null,
  text TEXT not null,
  title varchar(63) not null,
  type varchar(255),
  user_id bigint,
  primary key (id)
);

alter table app_user
    add constraint UK_1j9d9a06i600gd43uu3km82jw unique (email);

alter table app_user
    add constraint UK_3k4cplvh82srueuttfkwnylq0 unique (username);

alter table confirmation_token
    add constraint FKo9fl25wqyh7w7mnfkdqen1rcm foreign key (app_user_id) references app_user;

alter table one_time_code
    add constraint FK2orbqd7akobuewtlwwjv5mpb8 foreign key (app_user_id) references app_user;

insert into app_user (created_on, version, app_user_role, email, enabled, locked, password, username, id)
values
    (now(), 0, 'ADMIN', 'admin@edu.pl', true, false, '$2a$10$zeO42jMBl3pI2hj8Ylp8p.Kds5UrQ80Sh30Bg0MmiCKPpoTGvlDjK', 'admin', user_sequence.nextval),
    (now(), 0, 'USER', 'matz@edu.pl', true, false, '$2a$10$zeO42jMBl3pI2hj8Ylp8p.Kds5UrQ80Sh30Bg0MmiCKPpoTGvlDjK', 'user', user_sequence.nextval),
    (now(), 0, 'ADMIN', 'moderator@edu.pl', true, false, '$2a$10$zeO42jMBl3pI2hj8Ylp8p.Kds5UrQ80Sh30Bg0MmiCKPpoTGvlDjK', 'moderator', user_sequence.nextval);

insert into work
(created_by, created_on, modified_by, modified_on, version, rating, status, text, title, type, user_id, id)
values
    (2, now(), null, null, 0, 7.50, 'ACCEPTED',
     'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus placerat nec metus quis suscipit. ' ||
     'Aenean dictum tempus nibh in consectetur. Etiam ut lobortis elit. Sed sit amet lectus et sem euismod molestie non at eros. ' ||
     'Quisque nec efficitur libero, mollis porttitor metus. Nunc nec lacinia magna, in malesuada urna. ' ||
     'Aliquam dui erat, fermentum ac dolor sit amet, porttitor eleifend enim. Proin turpis eros, dapibus id tortor non, ' ||
     'rhoncus tincidunt neque. Nullam eu ex semper, ultrices neque at, fermentum nibh. Suspendisse interdum enim in ' ||
     'magna venenatis faucibus. In vel augue tristique, sollicitudin est ut, laoreet nisl. Orci varius natoque penatibus ' ||
     'et magnis dis parturient montes, nascetur ridiculus mus. In eu aliquam dolor, ac ultricies sapien. ' ||
     'Pellentesque vitae elit eget tellus pellentesque pretium vel vitae neque. In cursus odio ut odio pulvinar, eu ' ||
     'vestibulum tellus bibendum. Donec eget turpis consectetur, vulputate neque tempor, tempus nisi.',
     'Test 1', 'FANTASY', 2, work_sequence.nextval),
    (2, now(), null, null, 0, 7.00, 'ACCEPTED',
     'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus placerat nec metus quis suscipit. ' ||
     'Aenean dictum tempus nibh in consectetur. Etiam ut lobortis elit. Sed sit amet lectus et sem euismod molestie non at eros. ' ||
     'Quisque nec efficitur libero, mollis porttitor metus. Nunc nec lacinia magna, in malesuada urna. ' ||
     'Aliquam dui erat, fermentum ac dolor sit amet, porttitor eleifend enim. Proin turpis eros, dapibus id tortor non, ' ||
     'rhoncus tincidunt neque. Nullam eu ex semper, ultrices neque at, fermentum nibh. Suspendisse interdum enim in ' ||
     'magna venenatis faucibus. In vel augue tristique, sollicitudin est ut, laoreet nisl. Orci varius natoque penatibus ' ||
     'et magnis dis parturient montes, nascetur ridiculus mus. In eu aliquam dolor, ac ultricies sapien. ' ||
     'Pellentesque vitae elit eget tellus pellentesque pretium vel vitae neque. In cursus odio ut odio pulvinar, eu ' ||
     'vestibulum tellus bibendum. Donec eget turpis consectetur, vulputate neque tempor, tempus nisi.',
     'Test 1', 'FANTASY', 2, work_sequence.nextval),
    (2, now(), null, null, 0, 3.50, 'ACCEPTED',
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus placerat nec metus quis suscipit. ' ||
    'Aenean dictum tempus nibh in consectetur. Etiam ut lobortis elit. Sed sit amet lectus et sem euismod molestie non at eros. ' ||
    'Quisque nec efficitur libero, mollis porttitor metus. Nunc nec lacinia magna, in malesuada urna. ' ||
    'Aliquam dui erat, fermentum ac dolor sit amet, porttitor eleifend enim. Proin turpis eros, dapibus id tortor non, ' ||
    'rhoncus tincidunt neque. Nullam eu ex semper, ultrices neque at, fermentum nibh. Suspendisse interdum enim in ' ||
    'magna venenatis faucibus. In vel augue tristique, sollicitudin est ut, laoreet nisl. Orci varius natoque penatibus ' ||
    'et magnis dis parturient montes, nascetur ridiculus mus. In eu aliquam dolor, ac ultricies sapien. ' ||
    'Pellentesque vitae elit eget tellus pellentesque pretium vel vitae neque. In cursus odio ut odio pulvinar, eu ' ||
    'vestibulum tellus bibendum. Donec eget turpis consectetur, vulputate neque tempor, tempus nisi.',
    'Kryminał', 'CRIME', 2, work_sequence.nextval);

insert into comment (created_by, created_on, modified_by, modified_on, version, text, user_id, visible, work_id, id)
values
    (1, now(), null, null, 0, 'Bardzo fajne.', 1, true, 1, comment_sequence.nextval),
    (2, now(), null, null, 0, 'Może być.', 2, true, 1, comment_sequence.nextval),
    (2, now(), null, null, 0, 'Słabe.', 2, true, 3, comment_sequence.nextval),
    (2, now(), null, null, 0, 'Beznadzieja. Naucz się pisać.', 2, false, 3, comment_sequence.nextval);

insert into rating (id, created_by, created_on, modified_by, modified_on, version, rate, user_id, work_id)
values
    (rating_sequence.nextval, 1, now(), null, null, 0, 7, 1, 1),
    (rating_sequence.nextval, 1, now(), null, null, 0, 8, 3, 1),
    (rating_sequence.nextval, 1, now(), null, null, 0, 1, 2, 3),
    (rating_sequence.nextval, 1, now(), null, null, 0, 6, 1, 2),
    (rating_sequence.nextval, 1, now(), null, null, 0, 8, 3, 2),
    (rating_sequence.nextval, 1, now(), null, null, 0, 6, 2, 3)



