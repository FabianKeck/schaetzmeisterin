export const getPlayerData = (game, playerId) =>
  game.betSession.players.find((player) => player.id === playerId);

export const getMinBet = (game, playerId) => {
  const playerBet = getPlayerData(game, playerId).currentBet;
  return (
    Math.max(...game.betSession.players.map((player) => player.currentBet)) -
    playerBet
  );
};

export const isActive = (game, playerId) =>
  playerId === game.betSession.players[game.betSession.activePlayerIndex].id;

export const getPot = (game) =>
  game.betSession.players
    .map((player) => player.currentBet)
    .reduce((sum, currentBet) => sum + currentBet);
