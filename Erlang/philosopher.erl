%% @author nebbe
%% @doc @todo Add description to philosopher.


-module(philosopher).

%% ====================================================================
%% API functions
%% ====================================================================
-export([start/5]).

start(Times,Left,Right,Name,Parent) ->
	spawn_link(fun() -> action(Times,Left,Right,Name,Parent) end).

%% ====================================================================
%% Internal functions
%% ====================================================================

action(Times,Left,Right,Name,Parent) when Times>0 ->
	random:seed(erlang:now()),
 	timer:sleep(random:uniform(100)), % Wait
	io:format("~nPhilosopher ~p wants to eat ",[self()]),		
	Left ! {self(),request},		
	case receivemsg(Left,pt()+500,pt(),1) of
		giveup ->
			action(Times,Left,Right,Name,Parent);
		granted ->
			Right ! {self(),request},
			case receivemsg(Right,pt()+500,pt(),1) of
				giveup ->
					returnStick(Left),
					action(Times,Left,Right,Name,Parent);
				granted -> 
					io:format("~nPhilosopher ~p is eating ",[self()]),
					timer:sleep(random:uniform(500)), % Eat
					returnStick(Left),
					returnStick(Right),
					action(Times-1,Left,Right,Name,Parent)
			end
	end;
action(_,_,_,_,Parent) ->
	io:format("~nPhilosopher ~p is done! ",[self()]),
	Parent ! done.


%% 	Left ! {self(),request},
%% 	Right ! {self(),request},
%% 	case receivemsg(Left,pt()+500,pt(),1) of
%% 		giveup ->
%% 			case receivemsg(Right,pt()+20,pt(),1) of 
%% 				giveup ->					
%% 					timer:sleep(400),
%% 					action(Times,Left,Right,Name,Parent);
%% 				granted ->
%% 					returnStick(Right),
%% 					timer:sleep(400),
%%  					action(Times,Left,Right,Name,Parent)
%% 				end;
%% 		granted ->
%% 			case receivemsg(Right,pt()+20,pt(),1) of 
%% 				giveup ->
%% 					returnStick(Left),
%% 					timer:sleep(400),
%% 					action(Times,Left,Right,Name,Parent);
%% 				granted ->
%% 					io:format("~nPhilosopher ~p is eating ",[self()]),
%% 					timer:sleep(random:uniform(500)), % Eat
%% 					returnStick(Left),
%% 					returnStick(Right),
%% 					action(Times-1,Left,Right,Name,Parent)
%% 				end
%% 	end;	

returnStick(Stick) ->
	Stick ! {self(),return},
	io:format("~nChopstick ~p returned from ~p ",[Stick,self()]).
	
receivemsg(Stick,Endtime,Presenttime,Printed) -> % Will loop until decided to give up
	receive
		{Stick,granted} ->
			io:format("~nChopstick ~p granted to ~p ",[Stick,self()]),
			granted;
		{Stick,denied} when Presenttime > Endtime ->
			io:format("~nPhilosopher ~p gives up on getting ~p ",[self(),Stick]),
			giveup;
		{Stick,denied} when Printed==1-> 
			io:format("~nChopstick ~p denied to ~p ",[Stick,self()]),
			Stick ! {self(),request},
			receivemsg(Stick,Endtime,pt(),0);
		{Stick,denied} ->
			Stick ! {self(),request},
			receivemsg(Stick,Endtime,pt(),0)
	end.

pt() ->
	gettime(erlang:now()).
gettime({_,_,Time}) -> Time.			










