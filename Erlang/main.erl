%% @author nebbe
%% @doc @todo Add description to labb3.


-module(labb3).

%% ====================================================================
%% API functions
%% ====================================================================
-export([start/0]).

start() ->
	spawn(fun() -> init() end).

init() ->
	Start = erlang:now(),
	C1 = chopstick:start(),
	C2 = chopstick:start(),
	C3 = chopstick:start(),
	C4 = chopstick:start(),
	C5 = chopstick:start(),
 	philosopher:start(5, C1,C2, "Arendt", self()),
 	philosopher:start(5, C2,C3, "Hypatia", self()),
 	philosopher:start(5, C3,C4, "Simone", self()),
  	philosopher:start(5, C4,C5, "Elizabeth", self()),
  	philosopher:start(5, C5,C1, "Ayn", self()),
   	wait(5, [C1, C2, C3, C4, C5],Start).


wait(0, Chopsticks,Start) ->
	Stop=erlang:now(),
	io:format("Time: ~p~n", [getTime(Start,Stop)]),
	lists:foreach(fun(C) -> chopstick:quit(C) end, Chopsticks);
wait(N, Chopsticks,Start) ->
	receive
		done ->
			wait(N-1, Chopsticks,Start);
		abort ->
			exit(abort)
	end.		

getTime({_,Start,_},{_,Stop,_}) ->
	Stop-Start.

%% ====================================================================
%% Internal functions
%% ====================================================================


