import React, { useContext, useEffect, useState } from 'react';
import Header from '../commons/Header';
import UserContext from '../../context/UserContext';
import { getGame, betPost } from '../../service/GameService';
import { useParams } from 'react-router-dom';

export default function PlayPage() {
  const { userData, token } = useContext(UserContext);
  const { gameid } = useParams();
  const [game, setGame] = useState({});
  useEffect(() => {
    const id = setInterval(
      () => getGame(token, gameid).then((response) => setGame(response.data)),
      2000
    );
    return () => {
      clearTimeout(id);
    };
  }, [gameid, token]);

  return (
    <>
      <Header>Playing</Header>
      <p>{JSON.stringify(getPlayerData)}</p>
      {game?.players[game.activePlayerIndex].id === userData.playerId && (
        <button onClick={handleBet}>bet!</button>
      )}
    </>
  );
  function getPlayerData() {
    return game?.players?.find((player) => player.id === userData.playerId);
  }
  function handleBet() {
    setGame({ ...game, activePlayerIndex: -1 });
    betPost(token, gameid);
  }
}
