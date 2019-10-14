import itertools

import numpy as np
from glumpy import app, gl, glm, gloo


def project(x, y, z, d):
    return [(x * d) / (z + d), (y * d) / (z + d)]


def main() -> None:
    vertex = """
    attribute vec3 position;
    attribute vec4 color;
    
    uniform mat4 model;
    uniform mat4 view;
    uniform mat4 projection;
    
    varying vec4 v_color;
    
    void main()
    {
        gl_Position = projection * view * model * vec4(position, 1.0);
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

    window = app.Window(color=(0, 0, 0, 1))

    phi = 0.0
    theta = 0.0
    omega = 0.0
    dx = 0.0
    dy = 0.0
    dz = 0.0
    scale = 1.0
    d = 2

    @window.event
    def on_init():
        gl.glEnable(gl.GL_DEPTH_TEST)

    @window.event
    def on_resize(width, height):
        cube['projection'] = glm.perspective(45.0, width / float(height), 2.0, 100.0)

    @window.event
    def on_draw(dt):
        nonlocal phi, theta, omega, dx, dy, dz, scale, d

        model = np.eye(4, 4, dtype=np.float32)
        glm.xrotate(model, omega)
        glm.yrotate(model, phi)
        glm.zrotate(model, theta)
        glm.scale(model, scale, scale, scale)
        glm.translate(model, dx, dy, dz)

        cube['model'] = model

        window.clear()
        cube.draw(gl.GL_LINE_LOOP, I)

    @window.event
    def on_key_press(symbol, modifiers):
        nonlocal phi, theta, omega, dx, dy, dz, scale, d
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
        elif symbol == 76:  # L
            d += 0.1
        elif symbol == 75:  # K
            d -= 0.1
        elif symbol == 32:  # space
            phi = 0.0
            theta = 0.0
            omega = 0.0
            dx = 0.0
            dy = 0.0
            dz = 0.0
            scale = 1.0
            d = 2

    V = np.zeros(8, [('position', np.float32, 3),
                     ('color', np.float32, 4)])
    V['position'] = [[1, 1, 1], [-1, 1, 1], [-1, -1, 1], [1, -1, 1],
                     [1, -1, -1], [1, 1, -1], [-1, 1, -1], [-1, -1, -1]]
    V['color'] = [[0, 1, 1, 1], [0, 0, 1, 1], [1, 1, 1, 1], [0, 1, 0, 1],
                  [1, 1, 0, 1], [1, 1, 1, 1], [1, 0, 1, 1], [1, 0, 0, 1]]
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
    cube['view'] = glm.translation(0, 0, -5)
    cube.bind(V)

    app.run(framerate=60)


if __name__ == '__main__':
    main()
