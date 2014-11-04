//test
function test() {
	var grid = new Grid(4);
	grid.cells[1][0] = new Tile({x:1,y:0},4);
	grid.cells[2][0] = new Tile({x:2,y:0},2);
	grid.cells[3][0] = new Tile({x:3,y:0},2);
	grid.cells[2][1] = new Tile({x:2,y:1},8);
	grid.cells[3][1] = new Tile({x:3,y:1},8);
	grid.cells[3][2] = new Tile({x:3,y:2},4);
	grid.cells[0][3] = new Tile({x:0,y:3},2);

	console.log(grid.toString());
	console.log("islands:"+grid.islands());  // 7
	console.log("availableCells:"+grid.availableCells().length);  //9
	console.log("smoothness:"+grid.smoothness());  //-6
	console.log("monotonicity2:"+grid.monotonicity2());  //-7


	var grid1 = new Grid(4);
	
	for (var x = 0; x < 4; x++) {
		for (var y = 0; y < 4; y++) {
			grid1.cells[x][y] = new Tile({x:x,y:y},4);
		};
	};

	console.log(grid1.toString());
	console.log("islands:"+grid1.islands()); 

	var grid2 = new Grid(4);
	
	// for (var x = 0; x < 4; x++) {
	// 	for (var y = 0; y < 4; y++) {
	// 		grid1.cells[x][y] = new Tile({x:x,y:y},4);
	// 	};
	// };

	console.log(grid2.toString());
	console.log("islands:"+grid2.islands()); 

}