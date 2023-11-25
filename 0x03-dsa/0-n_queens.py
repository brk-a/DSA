'''
there is an N * N chessboard. the  idea is to
place N queens such that said queens cannot
attack each other

given an integer, N, return all  distinct
solutions in any order
'''


class Solution:
    """ solve the n-queens problem """
    def solveNQueens(self, n:int) -> List[List[str]]:
        """entry point"""
        solutions = []
        state = []
        self.search(state, solutions, n)
        return solutions

    def is_valid_state(self, state, n):
        """ check if state is valid """
        return len(state) == n
    
    def get_candidates(self, state, n):
        """get potential solutions"""
        if not state return range(n)

        position = len(state)
        candidates = set(range(n))

        for row, col in enumerate(state):
            candidates.discard(col)

            dist = position - row
            candidates.discard(col + dist)
            candidates.discard(col - dist)
        
        return candidates

    def search(self, state, solutions, n):
        """  """
        if is_valid_state(state, n):
            state_string = state_to_string(state, n)
            solutions.append()
            return
        
        for candidate in self.get_candidates(state, n):
            state.append(candidate, n)
            self.search(state, solutions, n)
            state.pop()
        
        return

    def state_to_string(self, state, n):
        """convert state into string format"""
        result = []
        for i in state:
            string = f"{'.'*i}Q{'.'*(n-i-1)}"