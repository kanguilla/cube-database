
--CREATE DATABASE TABLES
--=======================
drop table if exists cards;
create table if not exists cards(
      name varchar(50) primary key not NULL,
	  cost varchar(10),
	  typeline varchar(50),
	  color varchar (5),
	  power int,
	  toughness int,
	  rarity char(1),
	  mset char(3)
	  );
	  
drop table if exists deckLists;
create table if not exists deckLists(
      cardName varchar(50) references cards(name),
	  deckName varchar(50) references decks(name),
	  occurences int
	  );
	  
drop table if exists decks;
create table if not exists decks(
      name varchar(50) primary key not NULL,
	  archetype varchar(50) references archetypes(name)
	  );
	  
drop table if exists archetypes;
create table if not exists archetypes(
      name varchar(50) primary key not NULL,
	  colors varchar(5)
	  );  
	  
drop table if exists players;
create table if not exists players(
	  id int primary key not NULL,
      name varchar(50) not NULL
	  );  
	  
drop table if exists playLog;
create table if not exists playLog(
	  playerId int references players(id),
	  deckName varchar(50) references decks(name),
	  wins int
	  );  
	  
drop table if exists archetypeMembers;
create table if not exists archetypeMembers(
	  cardName varchar(50) references cards(name),
	  archetypeName varchar(50) references archetypes(name)
	  );  
	  
begin transaction;

insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Stromkirk Captain', '1BR', 'Creature Vampire Soldier', 'BR', 2, 2, 'U', 'ISD');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Ghost Quarter', '', 'Land', '', NULL, NULL, 'U', 'DIS');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Necromantic Summons', '4B', 'Sorcery', 'B', NULL, NULL, 'U', 'ORI');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Disperse', '1U', 'Instant', 'U', NULL, NULL, 'C', 'LRW');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Observant Alseid', '2W', 'Enchantment Creature Nymph', 'W', 2, 2, 'C', 'THS');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Naturalize', '1G', 'Instant', 'G', NULL, NULL, 'C', 'M10');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Thopter Engineer', '2R', 'Creature Human Artificer', 'R', 1, 3, 'U', 'ORI');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Barony Vampire', '2B', 'Creature Vampire', 'B', 3, 2, 'C', 'M11');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Nearheath Stalker', '4R', 'Creature Vampire', 'R', 4, 1, 'C', 'DKA');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Night Revelers', '4R', 'Creature Vampire', 'R', 4, 4, 'C', 'ISD');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Dementia Sliver', '3UB', 'Creature Sliver', 'BU', 4, 1, 'U', 'TSP');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Herald of Kozilek', '1UR', 'Creature Eldrazi', 'UR', 2, 2, 'U', 'BFZ');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Flame-Kin Zealot', '1RRW', 'Creature Elemental Soldier', 'WR', 2, 2, 'U', 'RAV');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Nyx Weaver', '1GB', 'Enchantment Creature Spider', 'GB', 1, 3, 'U', 'JOU');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Boneyard Wurm', '1G', 'Creature Wurm', 'G', 0, 0, 'C', 'ISD');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Raised by Wolves', '3GG', 'Enchantment Aura', 'G', 4, 1, 'U', 'BNG');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Explore', '1G', 'Sorcery', 'G', NULL, NULL, 'C', 'WWK');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Might Makes Right', '5R', 'Enchantment', 'R', NULL, NULL, 'U', 'M15');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Goblin Glory Chaser', 'R', 'Creature Goblin Warrior', 'R', 1, 1, 'C', 'ORI');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Galvanic Blast', 'R', 'Instant', 'R', 1, 1, 'C', 'SOM');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Into the Core', '2RR', 'Instant', 'R', NULL, NULL, 'U', 'SOM');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Telekentic Sliver', '3UU', 'Creature Sliver', 'U', 2, 2, 'U', 'TSP');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Pull from the Deep', '2UU', 'Sorcery', 'U', NULL, NULL, 'U', 'JOU');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Vision Skeins', '1U', 'Instant', 'U', NULL, NULL, 'C', 'DIS');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Wall of Essence', '2W', 'Creature Wall', 'W', 0, 3, 'C', 'M12');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Loxodon Partisan', '4W', 'Creature Elephant Soldier', 'W', 4, 4, 'C', 'MBS');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Eternal Thirst', '1B', 'Enchantment Aura', 'B', NULL, NULL, 'U', 'ORI');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Inspired Charge', '2WW', 'Instant', 'W', NULL, NULL, 'C', 'M11');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Patrol Signaler', '2W', 'Creature Kithkin Soldier', 'W', 1, 2, 'C', 'LRW');
insert into cards(name, cost, typeline, color, power, toughness, rarity, mset) values ('Blazing Torch', '1', 'Artifact Equipment', '', NULL, NULL, 'U', 'ZEN');

end transaction;

begin transaction;

insert into players(id, name) values (1, 'Khalil');
insert into players(id, name) values (2, 'Annabel');
insert into players(id, name) values (3, 'Warner');
insert into players(id, name) values (4, 'Ben');
insert into players(id, name) values (5, 'Daniel');
insert into players(id, name) values (6, 'Melody');
insert into players(id, name) values (7, 'Meeko');

end transaction;

begin transaction;

insert into playLog(playerId, deckName, wins) values (1, 'Vampires', 3);
insert into playLog(playerId, deckName, wins) values (1, 'Artifact Control', 1);
insert into playLog(playerId, deckName, wins) values (2, 'Vampires', 5);
insert into playLog(playerId, deckName, wins) values (3, 'Cobra Flyers', 1);
insert into playLog(playerId, deckName, wins) values (5, 'Pillowfort', 1);
insert into playLog(playerId, deckName, wins) values (7, 'Aggro Tokens', 1);
insert into playLog(playerId, deckName, wins) values (4, 'Draw-Go', 4);
insert into playLog(playerId, deckName, wins) values (6, 'Slivers', 1);
insert into playLog(playerId, deckName, wins) values (4, 'Reanimator', 2);

end transaction;

begin transaction;

insert into archetypes(name, colors) values ('Politics', 'GWU');
insert into archetypes(name, colors) values ('Voltron', 'WUB');
insert into archetypes(name, colors) values ('Tribal', 'UBR');
insert into archetypes(name, colors) values ('Sacrifice', 'BRG');
insert into archetypes(name, colors) values ('Legion', 'RGW');
insert into archetypes(name, colors) values ('Rattlesnake', 'WBG');
insert into archetypes(name, colors) values ('Toolbox', 'URW');
insert into archetypes(name, colors) values ('Lurker', 'BGU');
insert into archetypes(name, colors) values ('Attrition', 'RWB');
insert into archetypes(name, colors) values ('Anarchy', 'GUR');

end transaction;

begin transaction;

insert into archetypeMembers(cardName, archetypeName) values ('Stromkirk Captain', 'Tribal');
insert into archetypeMembers(cardName, archetypeName) values ('Naturalize', 'Lurker');
insert into archetypeMembers(cardName, archetypeName) values ('Disperse', 'Lurker');
insert into archetypeMembers(cardName, archetypeName) values ('Ghost Quarter', 'Lurker');
insert into archetypeMembers(cardName, archetypeName) values ('Thopter Engineer', 'Toolbox');
insert into archetypeMembers(cardName, archetypeName) values ('Disperse', 'Toolbox');
insert into archetypeMembers(cardName, archetypeName) values ('Observant Alseid', 'Attrition');
insert into archetypeMembers(cardName, archetypeName) values ('Necromantic Summons', 'Sacrifice');
insert into archetypeMembers(cardName, archetypeName) values ('Observant Alseid', 'Voltron');
insert into archetypeMembers(cardName, archetypeName) values ('Barony Vampire', 'Tribal');
insert into archetypeMembers(cardName, archetypeName) values ('Barony Vampire', 'Lurker');
insert into archetypeMembers(cardName, archetypeName) values ('Dementia Sliver', 'Tribal');
insert into archetypeMembers(cardName, archetypeName) values ('Dementia Sliver', 'Arnachy');
insert into archetypeMembers(cardName, archetypeName) values ('Wall of Essence', 'Politics');
insert into archetypeMembers(cardName, archetypeName) values ('Wall of Essence', 'Voltron');
insert into archetypeMembers(cardName, archetypeName) values ('Wall of Essence', 'Rattlesnake');
insert into archetypeMembers(cardName, archetypeName) values ('Vision Skeins', 'Politics');
insert into archetypeMembers(cardName, archetypeName) values ('Vision Skeins', 'Anarchy');
insert into archetypeMembers(cardName, archetypeName) values ('Herald of Kozilek', 'Tribal');
insert into archetypeMembers(cardName, archetypeName) values ('Herald of Kozilek', 'Toolbox');
insert into archetypeMembers(cardName, archetypeName) values ('Thopter Engineer', 'Legion');
insert into archetypeMembers(cardName, archetypeName) values ('Telekentic Sliver', 'Tribal');
insert into archetypeMembers(cardName, archetypeName) values ('Pull from the Deep', 'Sacrifice');
insert into archetypeMembers(cardName, archetypeName) values ('Pull from the Deep', 'Lurker');
insert into archetypeMembers(cardName, archetypeName) values ('Pull from the Deep', 'Politics');
insert into archetypeMembers(cardName, archetypeName) values ('Pull from the Deep', 'Anarchy');
insert into archetypeMembers(cardName, archetypeName) values ('Raised by Wolves', 'Legion');
insert into archetypeMembers(cardName, archetypeName) values ('Nyx Weaver', 'Lurker');
insert into archetypeMembers(cardName, archetypeName) values ('Nyx Weaver', 'Sacrifice');
insert into archetypeMembers(cardName, archetypeName) values ('Necromantic Summons', 'Rattlesnake');
insert into archetypeMembers(cardName, archetypeName) values ('Night Revelers', 'Tribal');
insert into archetypeMembers(cardName, archetypeName) values ('Flame-Kin Zealot', 'Legion');
insert into archetypeMembers(cardName, archetypeName) values ('Into the Core', 'Attrition');
insert into archetypeMembers(cardName, archetypeName) values ('Eternal Thirst', 'Attrition');
insert into archetypeMembers(cardName, archetypeName) values ('Might Makes Right', 'Attrition');
insert into archetypeMembers(cardName, archetypeName) values ('Boneyard Wurm', 'Sacrifice');
insert into archetypeMembers(cardName, archetypeName) values ('Inspired Charge', 'Legion');
insert into archetypeMembers(cardName, archetypeName) values ('Patrol Signaler', 'Legion');

end transaction;

begin transaction;

insert into decks(name, archetype) values ('Vampires', 'Tribal');
insert into decks(name, archetype) values ('Hand Size', 'Lurker');
insert into decks(name, archetype) values ('Equipment', 'Toolbox');
insert into decks(name, archetype) values ('Aggro Tokens', 'Legion');

end transaction;

begin transaction;

insert into deckLists(deckName, cardName, occurences) values ('Vampires', 'Stromkirk Captain', 2);
insert into deckLists(deckName, cardName, occurences) values ('Vampires', 'Ghost Quarter', 3);
insert into deckLists(deckName, cardName, occurences) values ('Vampires', 'Barony Vampire', 1);
insert into deckLists(deckName, cardName, occurences) values ('Vampires', 'Necromantic Summons', 2);

insert into deckLists(deckName, cardName, occurences) values ('Hand Size', 'Pull from the Deep', 2);
insert into deckLists(deckName, cardName, occurences) values ('Hand Size', 'Eternal Thirst', 2);
insert into deckLists(deckName, cardName, occurences) values ('Hand Size', 'Vision Skeins', 2);
insert into deckLists(deckName, cardName, occurences) values ('Hand Size', 'Disperse', 1);

insert into deckLists(deckName, cardName, occurences) values ('Aggro Tokens', 'Raised by Wolves', 4);
insert into deckLists(deckName, cardName, occurences) values ('Aggro Tokens', 'Thopter Engineer', 4);
insert into deckLists(deckName, cardName, occurences) values ('Aggro Tokens', 'Inspired Charge', 2);
insert into deckLists(deckName, cardName, occurences) values ('Aggro Tokens', 'Patrol Signaler', 4);
insert into deckLists(deckName, cardName, occurences) values ('Aggro Tokens', 'Galvanic Blast', 1);

insert into deckLists(deckName, cardName, occurences) values ('Equipment', 'Galvanic Blast', 4);
insert into deckLists(deckName, cardName, occurences) values ('Equipment', 'Thopter Engineer', 2);
insert into deckLists(deckName, cardName, occurences) values ('Equipment', 'Blazing Torch', 1);
insert into deckLists(deckName, cardName, occurences) values ('Equipment', 'Herald of Kozilek', 2);

end transaction;