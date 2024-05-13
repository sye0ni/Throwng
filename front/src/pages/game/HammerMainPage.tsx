import React, { useRef, useEffect } from "react";
import p5 from "p5";
import "p5/lib/addons/p5.sound";
// import songA from "@/assets/audio/song.mp3";
// import nailPlaceA from "@/assets/audio/SFX_NailPlace.mp3";
// import nailHammerHitA from "@assets/audio/SFX_NailHammerHit.mp3";
import handImgA from "@/assets/images/hand.webp";
import nailTmgA from "@/assets/images/nail.webp";
import hammerImgA from "@/assets/images/handhammer.webp";

// p5.sound 라이브러리에 대한 타입 확장
declare module "p5" {
  interface SoundFile {
    play: (
      startTime?: number,
      rate?: number,
      amp?: number,
      cueStart?: number,
      duration?: number
    ) => void;
    stop: (startTime?: number) => void;
  }
  interface p5 {
    // loadSound: (path: string | File | Blob, successCallback?: (sound: SoundFile) => void, errorCallback?: (e: Error) => void, whileLoading?: (percentageLoaded: number) => void) => SoundFile;
  }
}

const HammerMainPage: React.FC = () => {
  const p5ContainerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    let p5Instance: p5;

    const sketch = (p: p5) => {
      let nails: Nail[] = [];
      // let song: p5.SoundFile,
      //   nailPlace: p5.SoundFile,
      //   nailHammerHit: p5.SoundFile;
      let handImg: p5.Image, nailImg: p5.Image, hammerImg: p5.Image;
      let handX = -30,
        handY = -40;
      let isHandPlacing = false;
      let hammerAngle = 0,
        isSpacePressed = false;

      class Nail {
        x: number;
        y: number;
        status: number;

        constructor(x: number, y: number) {
          this.x = x;
          this.y = y;
          this.status = 0; // 0: normal, 1: success
        }

        update() {
          this.x += 4.5;
        }

        display() {
          switch (this.status) {
            case 0:
              p.image(nailImg, this.x, this.y, 38, 96);
              break;
            case 1:
              p.image(nailImg, this.x, this.y + 85, 38, 96);
              break;
            default:
              p.image(nailImg, this.x, this.y, 38, 96);
              break;
          }
        }
      }

      p.preload = () => {
        // song = p.loadSound("@/assets/audio/song.mp3");
        // nailPlace = p.loadSound(nailPlaceA);
        // nailHammerHit = p.loadSound(nailHammerHitA);
        handImg = p.loadImage(handImgA);
        nailImg = p.loadImage(nailTmgA);
        hammerImg = p.loadImage(hammerImgA);
      };

      p.setup = () => {
        p.createCanvas(800, 500);
        p.frameRate(60);
        p.rectMode(p.CENTER);
        p.imageMode(p.CENTER);
        // song.play();
      };

      p.draw = () => {
        p.background(220, 221, 198); // 배경 색
        p.push();
        p.strokeWeight(3); // 직선 두께
        p.stroke(80);
        p.line(0, 395, 800, 395);
        p.pop();

        if (isHandPlacing) {
          handX += 25;
          handY += 25;

          if (handX > 140) {
            isHandPlacing = false;
          }
        } else {
          handX -= 25;
          handY -= 25;

          if (handX < -30) {
            handX = -30;
            handY = -40;
          }
        }

        p.image(handImg, handX, handY, 1024, 500);

        if (isSpacePressed && hammerAngle > -p.PI / 2) {
          nails.forEach((nail) => {
            if (nail.status === 0 && nail.x > 550 && nail.x < 600) {
              setTimeout(() => {
                // nailHammerHit.play();
                nail.status = 1;
              }, 25);
            }
          });

          hammerAngle -= p.PI / 15;

          if (hammerAngle < 0) {
            isSpacePressed = false;
          }
        } else if (!isSpacePressed && hammerAngle < p.PI / 2) {
          hammerAngle += p.PI / 15;
        }

        p.push();
        p.translate(1024, 300);
        p.rotate(hammerAngle);
        p.image(hammerImg, 0, 0, 1024, 1000);
        p.pop();

        nails.forEach((nail) => {
          nail.update();
          nail.display();
        });
      };

      p.keyPressed = () => {
        if (p.key === " ") {
          isSpacePressed = true;
        }
      };
    };

    if (p5ContainerRef.current) {
      p5Instance = new p5(sketch, p5ContainerRef.current);
    }

    return () => {
      if (p5Instance) {
        p5Instance.remove();
      }
    };
  }, []);

  return (
    <div ref={p5ContainerRef} style={{ width: "400px", height: "500px" }}></div>
  );
};

export default HammerMainPage;
