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


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument('dir')
    args = parser.parse_args()

    edges: Dict[str, Edge] = {k: Edge((v[0], v[1]), (v[2], v[3]), list(v[4:]))
                              for k, v in parse(f'{args.dir}/edge.csv').items()}

    vertices: Dict[str, Vertex] = {k: Vertex(float(v[0]), float(v[1]), float(v[2]), list(v[3:]))
                                   for k, v in parse(f'{args.dir}/vertex.csv').items()}

    faces: Dict[str, Face] = {k: Face(list(v))
                              for k, v in parse(f'{args.dir}/face.csv').items()}

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


if __name__ == '__main__':
    main()
