import argparse
from typing import List, Dict, NamedTuple, Tuple, Set

from draw import draw

Edge = NamedTuple('Edge', [('vertices', Tuple[str, str]),
                           ('faces', Tuple[str, str]),
                           ('edges', List[str])])

Vertex = NamedTuple('Vertex', [('x', float),
                               ('y', float),
                               ('z', float),
                               ('edges', List[str])])

Face = NamedTuple('Face', [('edges', List[str])])


def parse(file: str) -> Dict[str, List[str]]:
    with open(file) as fp:
        lines = fp.readlines()

    dct = dict()
    for line in lines:
        parts = line.strip().split(',')
        dct[parts[0]] = parts[1:]

    return dct


def show(vertices, edges, faces) -> None:
    V = []
    for vertex in vertices.values():
        V.append([vertex.x, vertex.y, vertex.z])

    I = []
    for face in faces.values():
        points: Set[str] = set()
        for edge in [edges[i] for i in face.edges]:
            points.update(edge.vertices)

        for p in points:
            I.append(int(p.replace('v', '')))

    draw(V, I)


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument('dir')
    parser.add_argument('--show', action='store_true')
    parser.add_argument('--edge')
    parser.add_argument('--face')
    parser.add_argument('--vertex', nargs='+')
    parser.add_argument('--edges', action='store_true')
    parser.add_argument('--faces', action='store_true')
    parser.add_argument('--vertices', action='store_true')
    args = parser.parse_args()

    edges: Dict[str, Edge] = {k: Edge((v[0], v[1]), (v[2], v[3]), list(v[4:]))
                              for k, v in parse(f'{args.dir}/edge.csv').items()}

    vertices: Dict[str, Vertex] = {k: Vertex(float(v[0]), float(v[1]), float(v[2]), list(v[3:]))
                                   for k, v in parse(f'{args.dir}/vertex.csv').items()}

    faces: Dict[str, Face] = {k: Face(list(v))
                              for k, v in parse(f'{args.dir}/face.csv').items()}

    # noinspection PyShadowingNames
    def find_face_vertices(face: Face) -> Set[str]:
        return {vertex_key
                for edge in [edges[i] for i in face.edges]
                for vertex_key in edge.vertices}

    if args.show:
        show(vertices, edges, faces)
    elif args.vertex and len(args.vertex) == 1 and args.vertices:
        vertex_key = args.vertex[0]
        _ = vertices[vertex_key]
        connected_vertices: Set[str] = set()
        for edge in edges.values():
            if vertex_key in edge.vertices:
                connected_vertices.update(edge.vertices)
        print(sorted(connected_vertices - {vertex_key}))
    elif args.face and args.vertices:
        face = faces[args.face]
        print(sorted(find_face_vertices(face)))
    elif args.face and args.edges:
        print(sorted(faces[args.face].edges))
    elif args.vertex and len(args.vertex) == 1 and args.faces:
        vertex_key = args.vertex[0]
        _ = vertices[vertex_key]
        faces_key_set: Set[str] = set()
        for edge in edges.values():
            if vertex_key in edge.vertices:
                faces_key_set.update(edge.faces)
        print(sorted(faces_key_set))
    elif args.edge and args.vertices:
        edge = edges[args.edge]
        print(sorted(edge.vertices))
    elif args.vertex and len(args.vertex) == 3:
        vertices_face_dct = dict()
        for face_key, face in faces.items():
            vertices_face_dct[','.join(sorted(find_face_vertices(face)))] = face_key
        print(vertices_face_dct[','.join(sorted(args.vertex))])
    else:
        print('invalid argument')


if __name__ == '__main__':
    main()
