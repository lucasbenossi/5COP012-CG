from typing import List, Dict

import numpy as np
from glumpy import app, gl, glm, gloo


def draw(vertices, indexes) -> None:
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

    V = np.zeros(len(vertices), [('position', np.float32, 3)])
    V['position'] = vertices
    V = V.view(gloo.VertexBuffer)

    I = np.array(indexes, dtype=np.uint32)
    I = I.view(gloo.IndexBuffer)

    cube = gloo.Program(vertex, fragment)
    cube['view'] = glm.translation(0, 0, -5)
    cube['color'] = [1.0, 0.0, 0.0, 1.0]
    cube.bind(V)

    app.run(framerate=60)


def parse(file: str) -> Dict[str, List[str]]:
    with open(file) as fp:
        lines = fp.readlines()

    dct = dict()
    for line in lines:
        parts = line.strip().split(',')
        dct[parts[0]] = parts[1:]

    return dct


def main() -> None:
    edge_dct = parse('cube/edge.csv')
    vertex_dct = parse('cube/vertex.csv')
    face_dct = parse('cube/face.csv')

    V = []
    for vertex in vertex_dct.values():
        V.append([float(vertex[0]), float(vertex[1]), float(vertex[2])])

    I = []
    for face in face_dct.values():
        points = set()
        for edge in [edge_dct[i] for i in face]:
            points.add(edge[0])
            points.add(edge[1])

        for p in points:
            I.append(int(p.replace('v', '')))

    draw(V, I)


if __name__ == '__main__':
    main()
