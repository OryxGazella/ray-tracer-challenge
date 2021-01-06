Feature: Tuples, Vectors, and Points

  Scenario: A tuple with w=1.0 is a point
    Given a ← Tuple(4.3, -4.2, 3.1, 1.0)
    Then a = Tuple(x=4.3, y=-4.2, z=3.1, w=1.0)
    And a is not a "vector"
    And a is a "point"

  Scenario: A tuple with w=0.0 is a vector
    Given a ← Tuple(4.3, -4.2, 3.1, 0.0)
    Then a = Tuple(x=4.3, y=-4.2, z=3.1, w=0.0)
    And a is not a "point"
    And a is a "vector"

  Scenario: Point() creates tuples with w=1
    Given p ← point(4, -4, 3)
    Then p = Tuple(x=4.0, y=-4.0, z=3.0, w=1.0)

  Scenario: Vector() creates tuples with w=0
    Given v ← vector(4, -4, 3)
    Then v = Tuple(x=4.0, y=-4.0, z=3.0, w=0.0)

  Scenario: Adding two tuples
    Given a1 ← Tuple(3, -2, 5, 1)
    And a2 ← Tuple(-2, 3, 1, 0)
    Then  a1 + a2 = Tuple(x=1.0, y=1.0, z=6.0, w=1.0)

  Scenario: Subtracting two points
    Given p1 ← point(3, 2, 1)
    And   p2 ← point(5, 6, 7)
    Then  p1 - p2 = "Tuple(x=-2.0, y=-4.0, z=-6.0, w=0.0)"

  Scenario: Subtracting a vector from a point
    Given p ← point(3, 2, 1)
    And   v ← vector(5, 6, 7)
    Then  p - v = "Tuple(x=-2.0, y=-4.0, z=-6.0, w=1.0)"

  Scenario: Subtracting a vector from a vector
    Given v1 ← vector(3, 2, 1)
    And v2 ← vector(5, 6, 7)
    Then v1 - v2 = "Tuple(x=-2.0, y=-4.0, z=-6.0, w=0.0)"

  Scenario: Subtracting a vector from the zero vector
    Given z ← vector(0, 0, 0)
    And v ← vector(1, -2, 3)
    Then z - v = "Tuple(x=-1.0, y=2.0, z=-3.0, w=0.0)"

  Scenario: Negating a tuple
    Given a ← Tuple(1, -2, -3, 4)
    Then -a = "Tuple(x=-1.0, y=2.0, z=3.0, w=-4.0)"

  Scenario: Multiplying a tuple by a scalar
    Given a ← Tuple(1, -2, 3, -4)
    When b ← a * 3.5
    Then b = Tuple(x=3.5, y=-7.0, z=10.5, w=-14.0)

  Scenario: Multiplying a tuple by a scalar commutes
    Given a ← Tuple(1, -2, 3, -4)
    When b ← 3.5 * a
    Then b = Tuple(x=3.5, y=-7.0, z=10.5, w=-14.0)

  Scenario: Dividing a tuple by a scalar
    Given a ← Tuple(1, -2, 3, -4)
    When b ← a / 2
    Then b = Tuple(x=0.5, y=-1.0, z=1.5, w=-2.0)

  Scenario Outline: Computing the magnitude of vector(1, 0, 0)
    Given v ← <v>
    Then magnitude(v) = <magnitude>
    Examples:
      | v                 | magnitude |
      | vector(1, 0, 0)   | 1         |
      | vector(0, 1, 0)   | 1         |
      | vector(0, 0, 1)   | 1         |
      | vector(1, 2, 3)   | 3.7416575 |
      | vector(-1, 2, -3) | 3.7416575 |

  Scenario: Normalizing vector(4, 0, 0) gives (1, 0, 0)
    Given v ← vector(4, 0, 0)
    And b ← v.normalize
    Then b = Tuple(x=1.0, y=0.0, z=0.0, w=0.0)

  Scenario: Magnitude of a normalized vector is 1.0
    Given v ← vector(1, 2, 3)
    And b ← v.normalize
    Then magnitude(b) = 1.0

  Scenario: The dot product of two tuples
    Given v1 ← vector(1, 2, 3)
    And v2 ← vector(2, 3, 4)
    Then v1 dot v2 = 20

  Scenario: The cross product of two vectors
    Given v1 ← vector(1, 2, 3)
    And v2 ← vector(2, 3, 4)
    Then v1 cross v2 = "Tuple(x=-1.0, y=2.0, z=-1.0, w=0.0)"

  Scenario Outline: Cross product of unit vectors derive other axes
    Given v1 ← <v1>
    And v2 ← <v2>
    Then v1 cross v2 = "<expected>"
    Examples:
      | v1              | v2              | expected                           |
      | vector(1, 0, 0) | vector(0, 1, 0) | Tuple(x=0.0, y=0.0, z=1.0, w=0.0)  |
      | vector(0, 1, 0) | vector(1, 0, 0) | Tuple(x=0.0, y=0.0, z=-1.0, w=0.0) |
      | vector(1, 0, 0) | vector(0, 0, 1) | Tuple(x=0.0, y=-1.0, z=0.0, w=0.0) |
      | vector(0, 0, 1) | vector(1, 0, 0) | Tuple(x=0.0, y=1.0, z=0.0, w=0.0)  |
      | vector(0, 1, 0) | vector(0, 0, 1) | Tuple(x=1.0, y=0.0, z=0.0, w=0.0)  |
      | vector(0, 0, 1) | vector(0, 1, 0) | Tuple(x=-1.0, y=0.0, z=0.0, w=0.0) |

  Scenario: Colors are (red, green, blue) tuples
    Given color ← Color(-0.5, 0.4, 1.7)
    Then color.red = -0.5
    And color.green = 0.4
    And color.blue = 1.7

  Scenario: Adding colors
    Given c1 ← Color(0.9, 0.6, 0.75)
    Given c2 ← Color(0.7, 0.1, 0.25)
    Then c1 + c2 = Color(1.6, 0.7, 1.0)

  Scenario: Subtracting colors
    Given c1 ← Color(0.9, 0.6, 0.75)
    Given c2 ← Color(0.7, 0.1, 0.25)
    Then c1 - c2 = Color(0.2, 0.5, 0.5)

  Scenario: Multiplying a color by a scalar
    Given c1 ← Color(0.2, 0.3, 0.4)
    Then c1 * 2 = Color(0.4, 0.6, 0.8)
    And 2 * c1 = Color(0.4, 0.6, 0.8)

  Scenario: Multiplying colors
    Given c1 ← Color(1.0, 0.2, 0.4)
    Given c2 ← Color(0.9, 1.0, 0.1)
    Then c1 * c2 = Color(0.9, 0.2, 0.04)