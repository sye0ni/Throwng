import ReactDOM from "react-dom/client";
import App from "./App.tsx";
import "./index.scss";
import React from "react";
import * as Sentry from "@sentry/react";
import { createRoutesFromChildren, matchRoutes, useLocation, useNavigationType } from "react-router-dom";

Sentry.init({
  dsn: "https://e59c6078b8161c146a11dc1478f67e84@o4507270245777408.ingest.de.sentry.io/4507270261112912",
  integrations: [
    Sentry.browserTracingIntegration(),
    Sentry.reactRouterV6BrowserTracingIntegration({
      useEffect: React.useEffect,
      useLocation,
      useNavigationType,
      createRoutesFromChildren,
      matchRoutes,
    }),
  ],
  tracesSampleRate: 0.25,
  tracePropagationTargets: [
    "localhost:5173",
    /^https:\/\/www\.sieum\.co\.kr/,
  ],
  replaysSessionSampleRate: 0.1,
  replaysOnErrorSampleRate: 1.0,
});

ReactDOM.createRoot(document.getElementById("root")!).render(<App />);
