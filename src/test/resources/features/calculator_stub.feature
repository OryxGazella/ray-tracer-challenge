Feature: Basic arithmetic
  A calculator should be able to add and subtract numbers

  Scenario Outline: Adding two numbers
    Given <augend> added to <addend>
    Then sum is equal to <sum>
    Examples:
      | augend | addend | sum |
      | 1      | 1      | 2   |
      | 2      | 3      | 5   |

