import React, { useEffect, useRef } from 'react';
import neo4j from 'neo4j-driver';
import { fabric } from 'fabric';

const TechTree = () => {
  const canvasRef = useRef(null);
  const driver = neo4j.driver(
    'bolt://localhost:7687',
    neo4j.auth.basic('username', 'password')
  );

  useEffect(() => {
    const canvas = new fabric.Canvas(canvasRef.current, {
      backgroundColor: '#F7F7F7',
      width: window.innerWidth,
      height: window.innerHeight,
    });
    canvas.selection = false;
    canvas.on('mouse:wheel', (opt) => {
      const delta = opt.e.deltaY;
      let zoom = canvas.getZoom();
      zoom = zoom + delta / 200;
      if (zoom > 20) zoom = 20;
      if (zoom < 0.01) zoom = 0.01;
      canvas.zoomToPoint({ x: opt.e.offsetX, y: opt.e.offsetY }, zoom);
      opt.e.preventDefault();
      opt.e.stopPropagation();
    });

    const fetchData = async () => {
      const session = driver.session();
      const result = await session.run(
        'MATCH (n:Node)-[r:LINK]->(m:Node) RETURN n, r, m'
      );
      session.close();
      const nodes = {};
      const edges = {};
      result.records.forEach((record) => {
        const n = record.get('n');
        const r = record.get('r');
        const m = record.get('m');
        nodes[n.identity.low] = {
          id: n.identity.low,
          label: n.properties.name,
          x: n.properties.x,
          y: n.properties.y,
        };
        edges[r.identity.low] = {
          id: r.identity.low,
          source: n.identity.low,
          target: m.identity.low,
        };
      });

      const nodeElements = [];
      for (const id in nodes) {
        const node = nodes[id];
        const circle = new fabric.Circle({
          radius: 30,
          fill: '#FFFFFF',
          stroke: '#000000',
          strokeWidth: 2,
          left: node.x,
          top: node.y,
        });
        const label = new fabric.Text(node.label, {
          fontSize: 16,
          fontFamily: 'Helvetica',
          left: node.x - 30,
          top: node.y + 30,
        });
        const group = new fabric.Group([circle, label], {
          id: node.id,
          left: node.x,
          top: node.y,
          hasBorders: false,
          hasControls: false,
          lockRotation: true,
          lockScalingX: true,
          lockScalingY: true,
          lockMovementX: true,
          lockMovementY: true,
          selectable: true,
        });
        group.on('selected', () => {
          const session = driver.session();
          session.run(
            `MATCH (n:Node)-[r:LINK]->(m:Node) WHERE ID(n) = ${node.id} RETURN n, r, m`
          ).then((result) => {
            result.records.forEach((record) => {
              const n = record.get('n');
              const r = record.get('r');
              const m = record.get('m');
              console.log(`${n.properties.name} ${r.properties.name} ${m.properties.name}`);
            });
          });
          session.close
        });
        nodeElements.push(group);

        canvas.add(group);
        canvas.add(label);
        canvas.add(circle);

      }
      const edgeElements = [];
      for (const id in edges) {
        const edge = edges[id];
        const line = new fabric.Line([
          nodes[edge.source].x,
          nodes[edge.source].y,
          nodes[edge.target].x,
          nodes[edge.target].y,
        ], {
          stroke: '#000000',
          strokeWidth: 2,
          selectable: false,
        });
        edgeElements.push(line);
        canvas.add(line);
      }