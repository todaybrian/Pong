//Brian Yan
//May 25, 2022
// Enum which holds the different collision types to avoid double collisions on same object
package todaybrian;

public enum CollisionType {
    TOP_WALL, BOTTOM_WALL, 
    PADDLE1 /* Player 1's (left) paddle */, 
    PADDLE2 /* Player 2's (right) paddle*/
}
