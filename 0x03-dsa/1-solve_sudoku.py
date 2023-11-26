'''
solve a sudoku puzzle

rules of sudoku
    1. each of the digits 1-9, inclusive, must
    occur exactly once in each row
    2. each of the digits 1-9, inclusive, must
    occur exactly once in each column
    3. each of the digits 1-9, inclusive, must
    occur exactly once in each 3*3 sub-grids (
    there are 9 sub-grids)

given a partially filled 9*9 grid, write a
programme to fill the empty cells based on the
rules above 
'''

from itertools import product


class Solution:
    """ solve sudoku puzzle """

    SHAPE = 9
    GRID = 3
    EMPTY = "."
    DIGITS = set([str(num) for num in range(1, SHAPE+1)])

    def solveSudoku(self, board: List[List[str]]) -> None:
        """ entry point """
        self.search(board)
    
    def is_valid_state(self, board):
        """ check if state is valid """
        for row in self.get_rows(board):
            if not set(row)==self.DIGITS:
                return False
        for col in self.get_cols(board):
            if not set(col)==self.DIGITS:
                return False
        for sub_grid in self.get_sub_grid(board):
            if not set(sub_grid) in self.DIGITS:
                return False
        return True

    def get_candidates(self, board, row, col):
        """get potential solutions"""
        used_digits = set()
        used_digits.update(self.get_kth_row(board, row))
        used_digits.update(self.get_kth_col(board, col))
        used_digits.update(self.get_sub_grid_at_row_col(board, row, col))
        used_digits -= set([self.EMPTY])

        candidates = self.DIGITS - used_digits

        return candidates
    
    def search(self, board):
        """  """
        if self.is_valid_state(board):
            return True
        
        for row_idx, row in enumerate(board):
            for col_idx, elem in enumerate(row):
                if elem==self.EMPTY:
                    for candidate in self.get_candidates(board, row_idx, col_idx):
                        board[row_idx][col_idx] = candidate
                        is_solved = self.search(board)
                        if is_solved:
                            return True
                        else:
                            board[row_idx][col_idx] = self.EMPTY
                    return False
        return True
    
    def get_cols(self, board):
        """fetch columns from the board"""
        for i in range(self.shape):
            result = [board[row][i] for row in range(self.SHAPE)]
            yield result

    def get_rows(self, board):
        """fetch rows from the board"""
        for i in range(self.SHAPE):
            yield board[i]

    def get_sub_grid(self, board):
        """fetch a sub-grid from the board"""
        for row in range(0, self.SHAPE, self.GRID):
            for col in range(0, self.SHAPE, self.GRID):
                grid = [board[r][c] for r, c in product(range(row, row+self.GRID), range(col, col+self.GRID)]
                yield grid

    def get_kth_col(self, board, k):
        """fetch kth column from the board"""
        return [board[row][k] for row in range(self.SHAPE)]

    def get_kth_row(self, board, k):
        """fetch kth row from the board"""
        return board[k]

    def get_sub_grid_at_row_col(self, board, row, col):
        """fetch a column from the board"""
        row = row // self.GRID * self.GRID
        col = col // self.GRID * self.GRID
        return [board[r][c] for r, c in product(range(row, row+self.GRID), range(col, col+self.GRID))]
