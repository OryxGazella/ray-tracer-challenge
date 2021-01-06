Feature: Canvas

  Scenario: Creating a canvas
    Given canvas ← Canvas(10, 20)
    Then canvas.width = 10
    And canvas.height = 20
    And every pixel of canvas is Color(0, 0, 0)

  Scenario: Writing pixels to a canvas
    Given canvas ← Canvas(10, 20)
    And red ← Color(1, 0, 0)
    When canvas.writePixel(2, 3, red)
    Then canvas.pixelAt(2, 3) = red

  Scenario: Constructing the PPM header
    Given canvas ← Canvas(5, 3)
    Then lines 1-3 of canvas.ppm are
    """
    P3
    5 3
    255
    """

  Scenario: Constructing the PPM pixel data
    Given canvas ← Canvas(5, 3)
    And tooHigh ← Color(1.5, 0, 0)
    And normal ← Color(0, 0.5, 0)
    And tooLow ← Color(-0.5, 0, 1)
    When canvas.writePixel(0, 0, tooHigh)
    When canvas.writePixel(2, 1, normal)
    When canvas.writePixel(4, 2, tooLow)
    Then lines 4-6 of canvas.ppm are
    """
    255 0 0 0 0 0 0 0 0 0 0 0 0 0 0
    0 0 0 0 0 0 0 128 0 0 0 0 0 0 0
    0 0 0 0 0 0 0 0 0 0 0 0 0 0 255
    """

  Scenario: Splitting long lines in PPM files
    Given canvas ← Canvas(12, 2)
    When every pixel of c is set to Color(1, 0.8, 0.6)
    Then lines 4-9 of canvas.ppm are
    """
    255 204 153 255 204 153 255 204 153 255 204 153 255 204 153
    255 204 153 255 204 153 255 204 153 255 204 153 255 204 153
    255 204 153 255 204 153
    255 204 153 255 204 153 255 204 153 255 204 153 255 204 153
    255 204 153 255 204 153 255 204 153 255 204 153 255 204 153
    255 204 153 255 204 153
    """

  Scenario: PPM files are terminated by a newline character
    Given canvas ← Canvas(5, 3)
    Then ppm ends with a newline