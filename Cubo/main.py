import itertools

import numpy as np
from glumpy import app, gl, glm, gloo


def project(x, y, z, d=2):
    return [(x * d) / (z + d), (y * d) / (z + d)]


def main() -> None:
    vertex = """
    attribute vec2 position;
    attribute vec4 color;
    varying vec4 v_color;
    void main()
    {
        gl_Position = vec4(position, 0.0, 1.0);
        v_color = color;
    }
    """

    fragment = """
    varying vec4 v_color;
    void main()
    {
        gl_FragColor = v_color;
    }
    """

    window = app.Window(width=512, height=512, color=(0, 0, 0, 1))

    phi = 0.0
    theta = 0.0
    omega = 0.0
    dx = 0.0
    dy = 0.0
    dz = 0.0
    scale = 1.0

    @window.event
    def on_draw(dt):
        nonlocal phi, theta, omega, dx, dy, dz, scale

        matrix = np.eye(4, dtype=np.float32)

        glm.rotate(matrix, phi, 0, 1, 0)
        glm.rotate(matrix, theta, 0, 0, 1)
        glm.rotate(matrix, omega, 1, 0, 0)
        glm.scale(matrix, scale, scale, scale)
        glm.translate(matrix, dx, dy, dz)

        result = [np.matmul([*p, 1], matrix)[0:3] for p in V]

        cube['position'] = [project(*p) for p in result]

        window.clear()
        cube.draw(gl.GL_LINE_LOOP, I)

    @window.event
    def on_key_press(symbol, modifiers):
        nonlocal phi, theta, omega, dx, dy, dz, scale
        if symbol == 45 and modifiers == 1:  # shift -
            scale -= 0.1
        elif symbol == 61 and modifiers == 1:  # shit +
            scale += 0.1
        elif symbol == 81:  # Q
            theta += 10.0
        elif symbol == 69:  # E
            theta -= 10.0
        elif symbol == 87:  # W
            dy += 0.1
        elif symbol == 65:  # A
            dx -= 0.1
        elif symbol == 83:  # S
            dy -= 0.1
        elif symbol == 68:  # D
            dx += 0.1
        elif symbol == 90:  # Z
            phi += 10.0
        elif symbol == 67:  # C
            phi -= 10.0
        elif symbol == 73:  # I
            omega += 10.0
        elif symbol == 80:  # P
            omega -= 10.0
        elif symbol == 32:
            phi = 0.0
            theta = 0.0
            omega = 0.0
            dx = 0.0
            dy = 0.0
            dz = 0.0
            scale = 1.0

    V = np.array([[.5, .5, .5], [-.5, .5, .5], [-.5, -.5, .5], [.5, -.5, .5],
                  [.5, -.5, -.5], [.5, .5, -.5], [-.5, .5, -.5], [-.5, -.5, -.5]])
    V = V.view(gloo.VertexBuffer)

    I = np.array([0, 1, 2,
                  0, 2, 3,
                  0, 3, 4,
                  0, 4, 5,
                  0, 5, 6,
                  0, 6, 1,
                  1, 6, 7,
                  1, 7, 2,
                  7, 4, 3,
                  7, 3, 2,
                  4, 7, 6,
                  4, 6, 5], dtype=np.uint32)
    I = I.view(gloo.IndexBuffer)

    cube = gloo.Program(vertex, fragment)
    cube['position'] = [project(*p) for p in V]
    cube['color'] = [[0, 1, 1, 1], [0, 0, 1, 1], [1, 1, 1, 1], [0, 1, 0, 1],
                     [1, 1, 0, 1], [1, 1, 1, 1], [1, 0, 1, 1], [1, 0, 0, 1]]

    app.run(framerate=60)


if __name__ == '__main__':
    main()