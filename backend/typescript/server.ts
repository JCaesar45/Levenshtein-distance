import express, { Application, Request, Response } from "express";
import cors from "cors";

interface LeadInput {
  name: string;
  company?: string;
  email: string;
  budget?: number;
  timeline?: string;
  message?: string;
  consent?: boolean;
}

const app: Application = express();

app.use(cors());
app.use(express.json({ limit: "100kb" }));

const EMAIL_RE = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;

function scoreLead(lead: LeadInput): number {
  let score = 0;

  if ((lead.name || "").trim().length >= 2) {
    score += 10;
  }

  if ((lead.company || "").trim().length >= 2) {
    score += 10;
  }

  if (EMAIL_RE.test(lead.email || "")) {
    score += 25;
  }

  score += Math.max(0, Math.min(5, Number(lead.budget) || 0)) * 8;

  const timeline = (lead.timeline || "").toLowerCase();

  if (timeline === "urgent") {
    score += 20;
  } else if (timeline === "quarter") {
    score += 12;
  } else if (timeline === "month") {
    score += 8;
  } else if (timeline === "later") {
    score += 3;
  }

  score += Math.min(20, Math.floor((lead.message || "").trim().length / 10));

  if (lead.consent) {
    score += 5;
  }

  return Math.min(100, score);
}

app.get("/", (req: Request, res: Response) => {
  res.json({ status: "ok" });
});

app.post("/api/leads", (req: Request, res: Response) => {
  const lead = req.body as LeadInput;

  if (
    !lead ||
    typeof lead.name !== "string" ||
    typeof lead.email !== "string" ||
    !EMAIL_RE.test(lead.email)
  ) {
    res.status(400).json({ error: "Invalid lead payload" });
    return;
  }

  const score = scoreLead(lead);
  const tier = score >= 75 ? "hot" : score >= 45 ? "warm" : "cold";

  res.json({ score, tier });
});

const port = process.env.PORT ? Number(process.env.PORT) : 8080;

app.listen(port, () => {
  console.log("API listening on " + port);
});
