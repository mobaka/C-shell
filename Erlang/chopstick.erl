%% @author nebbe
%% @doc @todo Add description to chopstick.


-module(chopstick).

%% ====================================================================
%% API functions
%% ====================================================================
-export([start/0]).

start() ->
	spawn_link(fun() -> available() end).

%% ====================================================================
%% Internal functions
%% ====================================================================

available() ->
	receive
		{Parent,request} ->
			Parent ! {self(),granted},
			gone();
		quit ->
			ok
	end.

gone() ->
	receive
		{Parent,request} ->
			Parent ! {self(),denied},
			gone();
		{Parent,return} ->
			Parent ! {self(),returned},
			available();
		quit ->
			ok
	end.





