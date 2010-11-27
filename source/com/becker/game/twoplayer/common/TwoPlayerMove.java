package com.becker.game.twoplayer.common;

import com.becker.common.Location;
import com.becker.common.util.Util;
import com.becker.game.common.board.Board;
import com.becker.game.common.GameContext;
import com.becker.game.common.board.GamePiece;
import com.becker.game.common.Move;

/**
 *  This base class describes a change in state from one board
 *  position to the next in a game.
 *
 *  Note: when I first created this class I used a freeList to recycle
 *  old moves and avoid unnecessary object creation. However, while profiling,
 *  I found that this was actually slower than jnot using it.
 *
 *  We could save significant space by removing some of these members,
 *  and reducing the size of the remaining ones. eg toRow, toCol can be byte, value can be float, etc.
 *
 *  @see Board
 *  @author Barry Becker
 */
public class TwoPlayerMove extends Move
{
    private static final String P1 = GameContext.getLabel("PLAYER1");
    private static final String P2 = GameContext.getLabel("PLAYER2");

    /** The location of the move. */
    protected Location toLocation_;

    /**
     * The is the more accurate evaluated value from point of view of p1
     * It gets inherited from its descendants. It would be the real (perfect)
     * value of the position if the game tree is complete (which rarely happens in practice)
     */
    private int inheritedValue_;

    /** true if player1 made the move. */
    private boolean player1_;
    
    /** This is the piece to use on the board. Some games only have one kind of piece. */
    private GamePiece piece_;

    /**
     * true if this move was generated during quiescent search.
     * Perhaps should not be in this class.
     */
    private boolean urgent_;

    /**  If true then this move is a passing move. */
    protected boolean isPass_ = false;

    /** True if the player has resigned with this move. */
    protected boolean isResignation_ = false;

    /** If true then this move is in the path to selected move.  The game tree viewer may highlight it. */
    private boolean selected_;

    /** This is a move that we anticipate will be made in the future. Will be rendered differently. */
    private boolean isFuture_;

    /** Some coments about how the score wwas computed. Used for debugging. */
    private String scoreDescription_ = null;


    /**
     * protected Constructor.
     * use the factory method createMove instead.
     */
    protected TwoPlayerMove()
    {}

    /**
     * create a move object representing a transition on the board.
     */
    protected TwoPlayerMove( int destinationRow, int destinationCol,
                    int val, GamePiece p ) {
        toLocation_ = new Location(destinationRow, destinationCol);

        setValue(val);
        inheritedValue_ = getValue();
        selected_ = false;
        piece_ = p;
        if (p!=null)
            player1_ = p.isOwnedByPlayer1();
        isPass_ = false;
    }

    /**
     * factory method for getting new moves. It uses recycled objects if possible.
     * @return the newly created move.
     */
    public static TwoPlayerMove createMove( int destinationRow, int destinationCol,
                                            int val, GamePiece piece ) {
        return new TwoPlayerMove(destinationRow, destinationCol, val, piece);
    }

    /**
     * factory method for getting new moves. It uses recycled objects if possible.
     * @return the newly created move.
     */
    public static TwoPlayerMove createMove( Location destinationLocation,
                                            int val, GamePiece piece )  {
        return new TwoPlayerMove(destinationLocation.getRow(), destinationLocation.getCol(), val, piece );
    }

    /**
     * @return  a deep copy.
     */
    public TwoPlayerMove copy()  {
        TwoPlayerMove cp = createMove( toLocation_, getValue(),  piece_ );
        cp.selected_ = selected_;
        cp.urgent_ = urgent_;
        return cp;
    }

    public final byte getToRow()  {
        return toLocation_.getRow();
    }

    public final byte getToCol() {
        return toLocation_.getCol();
    }

    public final Location getToLocation() {
        return toLocation_;
    }

    /**
     * We sort based on the statically evaluated board value
     * because the inherited value is not known yet.
     * @return  > 0 if move m is bigger, < 0 if smaller, =0 if equal
     */
    @Override
    public int compareTo( Move m ) {

        int result = super.compareTo(m);
        if (result != 0)
            return result;

        // break tie by row position
        TwoPlayerMove move = (TwoPlayerMove) m;
        if ( this.getToRow() < move.getToRow() )
            return -1;
        else if ( this.getToRow() > move.getToRow() )
            return 1;
        else {
            // if still tie, break using col position.
            if ( this.getToCol() < move.getToCol() )
                return -1;
            else if ( this.getToCol() > move.getToCol() )
                return 1;
            else {
                return 0;
          }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TwoPlayerMove)) return false;

        TwoPlayerMove that = (TwoPlayerMove) o;

        return player1_ == that.player1_ && toLocation_ != null && toLocation_.equals(that.toLocation_);
    }

    @Override
    public int hashCode() {
        int result = toLocation_ != null ? toLocation_.hashCode() : 0;
        result = 31 * result + getValue();
        result = 31 * result + (player1_ ? 1 : 0);
        return result;
    }

    /**
     * @return  true if the player (or computer) chose to pass this turn.
     */
    public final boolean isPassingMove() {
        return isPass_;
    }

    public final boolean isResignationMove() {
        return isResignation_;
    }

    public final boolean isPassOrResignation() {
        return isPass_ || isResignation_;
    }

    public int getInheritedValue() {
        return inheritedValue_;
    }

    public void setInheritedValue(int inheritedValue) {
        this.inheritedValue_ = inheritedValue;
    }

    public boolean isPlayer1() {
        return player1_;
    }

    public void setPlayer1(boolean player1) {
        this.player1_ = player1;
    }

    public GamePiece getPiece() {
        return piece_;
    }

    public void setPiece(GamePiece piece) {
        this.piece_ = piece;
    }

    public boolean isUrgent() {
        return urgent_;
    }

    public void setUrgent(boolean urgent) {
        this.urgent_ = urgent;
    }

    public boolean isSelected() {
        return selected_;
    }

    public void setSelected(boolean selected) {
        this.selected_ = selected;
    }

    public boolean isFuture() {
        return isFuture_;
    }

    public void setFuture(boolean future) {
        isFuture_ = future;
    }

    public String getScoreDescription() {
        return scoreDescription_;
    }

    public void setScoreDescription(String desc) {
        scoreDescription_ = desc;
    }

    /**
     * @return a string, which if executed will create a move identical to this instance.
     */
    public String getConstructorString() {
        String pieceCreator = "null";
        if (getPiece() != null) {
            pieceCreator = getPiece().isOwnedByPlayer1()? "PLAYER1_PIECE" : "PLAYER2_PIECE";
            //pieceCreator = "new GamePiece(" + getPiece().isOwnedByPlayer1() + ")";
        }
        return "TwoPlayerMove.createMove(new Location("
                +  getToLocation().getRow()  + ", " + getToLocation().getCol()  + "), " + getValue() + ", "
                + pieceCreator + "),";
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (piece_!=null)
            s.append( piece_.isOwnedByPlayer1()? P1 : P2 );

        s.append(" val:").append(Util.formatNumber(getValue()));
        s.append(" inhrtd:").append(Util.formatNumber(inheritedValue_));
        if (piece_!=null)
            s.append(" piece:").append(piece_.toString());
        //s.append(" sel:"+selected);
        s.append('(').append(toLocation_.toString()).append(')');
        if (urgent_)
            s.append(" urgent!");
        s.append(" ");
        return s.toString();
    }
}

