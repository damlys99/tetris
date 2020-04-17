package tetris;

public class Block {
	boolean[][] curBlock= new boolean[4][4];
	boolean[][] befRotate = new boolean[4][4];
	Block(){
	}
	
	public void setBlock(byte a) {
		for(byte x=0;x<4;x++)
			for(byte y=0;y<4;y++) {
				curBlock[x][y]=Blocks.getBlock(a)[y][x];
			}
		Area.col=a;
	}
	private void reverseColumns(boolean curBlock[][]) 
    { 
        for (int i = 0; i < curBlock[0].length; i++) 
            for (int j = 0, k = curBlock[0].length - 1; 
                 j < k; j++, k--) { 
                boolean temp = curBlock[j][i]; 
                curBlock[j][i] = curBlock[k][i]; 
                curBlock[k][i] = temp; 
            } 
    } 
  
    // Function for do transpose of matrix 
    private void transpose(boolean curBlock[][]) 
    { 
        for (int i = 0; i < curBlock.length; i++) 
            for (int j = i; j < curBlock[0].length; j++) { 
                boolean temp = curBlock[j][i]; 
                curBlock[j][i] = curBlock[i][j]; 
                curBlock[i][j] = temp; 
            } 
    }
    public void rotate(boolean curBlock[][]) 
    { 		for(byte x=0;x<4;x++)
		for(byte y=0;y<4;y++) {
			befRotate[x][y]=curBlock[x][y];
		}
        transpose(curBlock); 
        reverseColumns(curBlock); 
    } 
    public void unrotate() {
		for(byte x=0;x<4;x++)
			for(byte y=0;y<4;y++) {
				curBlock[x][y]=befRotate[x][y];
			}
    }
  
}
