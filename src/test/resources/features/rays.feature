Feature: Rays

  Scenario: Creating and querying a ray
    Given origin ← point(1, 2, 3)
    And direction ← vector(4, 5, 6)
    When r ← Ray(origin, direction)
    Then r.origin = origin
    And r.direction = direction


  Scenario: Computing a point from a distance
    Given r ← Ray(point(2, 3, 4), vector(1, 0, 0))
    Then r.position(0) = point(2, 3, 4)
    And r.position(1) = point(3, 3, 4)
    And r.position(-1) = point(1, 3, 4)
    And r.position(2.5) = point(4.5, 3, 4)

  Scenario: A ray intersects a sphere at two points
    Given r ← Ray(point(0, 0, -5), vector(0, 0, 1))
    And s ← Sphere()
    When xs ← s.intersects(r)
    Then xs.count = 2
    And xs[0] = 4.0
    And xs[1] = 6.0

  Scenario: A ray intersects a sphere at a tangent
    Given r ← Ray(point(0, 1, -5), vector(0, 0, 1))
    And s ← Sphere()
    When xs ← s.intersects(r)
    Then xs.count = 2
    And xs[0] = 5.0
    And xs[1] = 5.0

  Scenario: An intersection encapsulates t and object
    Given s ← Sphere()
    When i ← Intersection(3.5, s)
    Then i.t = 3.5
    And i.target = s

  Scenario: Intersect sets the object on the intersection
    Given r ← Ray(point(0, 0, -5), vector(0, 0, 1))
    And s ← Sphere()
    When xs ← s.intersects(r)
    Then xs.count = 2
    And xs[0].target = s
    And xs[1].target = s

  Scenario: The hit, when all intersections have positive t
    Given s ← Sphere()
    And i1 ← Intersection(1, s)
    And i2 ← Intersection(2, s)
    And xs ← Intersections:
      | i2 | i1 |
    When i ← hit(xs)
    Then i = i1

