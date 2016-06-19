package heigvd.nonograms.models

/**
  * Types of cells fot the game played by a user.
  */
sealed trait CellType;
// No current state (the default state for all cells)
case class None() extends CellType
// Current state is empty (the user believe the state is empty, and solution=false)
case class Empty() extends CellType
// Current state is filles (the user believe the state is filled, and solution=true)
case class Filled() extends CellType
// same as empty, but the user is not sure yet
case class MaybeEmpty() extends CellType
// same as filled, but the user is not sure yet
case class MaybeFilled() extends CellType
// same as empty, but the user tried to put (wrongly) a filled state: its a penalty state
case class Tried() extends CellType