import Header from "@components/Header";
import "@/styles/Content.scss";
import logo from "@/assets/images/backlogo.webp";
import hammer from "@/assets/images/Hammer.webp";
import game2 from "@/assets/images/Rockpaperscissors.webp";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getQuizContent } from "@services/contentApi/ContentApi";
import { QuizContent } from "@/types/quizType";

const ContentPage = () => {
  const navigate = useNavigate();
  const [quizContents, setQuizContents] = useState<QuizContent[]>([]);

  useEffect(() => {
    const fetchQuizContents = async () => {
      try {
        const data: QuizContent[] = await getQuizContent();
        setQuizContents(data);
      } catch (error) {
        console.error("Failed to fetch quiz contents:", error);
      }
    };

    fetchQuizContents();
  }, []);

  const handleGoQuiz = (status: boolean) => {
    if (!status) {
      navigate("/quiz/main", { replace: true });
    }
  };

  return (
    <div className="ContentPage">
      <Header centerText="컨텐츠" />
      <div className="content-item">
        {quizContents.map(
          (content) =>
            content.name === "quiz" && (
              <div
                key={content.name}
                className="quiz-border"
                onClick={() => handleGoQuiz(content.status)}
              >
                <div className="quiz-title">
                  <p>쓰롱-퀴즈</p>
                  <div
                    className={`participation ${
                      content.status ? "inactive" : ""
                    }`}
                  >
                    <p>{content.status ? "OFF" : "ON"}</p>
                  </div>
                </div>
                <div>
                  <img src={logo} alt="quiz" />
                </div>
              </div>
            )
        )}
        <div className="game1-border">
          <div className="game1-title">
            <p>리듬게임</p>
            <div className="participation">
              <p>OFF</p>
            </div>
          </div>
          <div>
            <img src={hammer} alt="game1" />
          </div>
        </div>
        <div className="game2-border">
          <div className="game2-title">
            <p>가위바위보?</p>
            <div className="participation">
              <p>OFF</p>
            </div>
          </div>
          <div>
            <img src={game2} alt="game2" />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ContentPage;