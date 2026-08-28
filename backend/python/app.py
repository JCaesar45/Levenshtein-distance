from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import re

app = FastAPI(title="Belfort Luxe Leads API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)

EMAIL_RE = re.compile(r"^[^@\s]+@[^@\s]+\.[^@\s]+$")


class Lead(BaseModel):
    name: str
    company: str = ""
    email: str
    budget: int = 0
    timeline: str = ""
    message: str = ""
    consent: bool = False


def score_lead(lead: Lead) -> int:
    score = 0

    if len(lead.name.strip()) >= 2:
        score += 10

    if len(lead.company.strip()) >= 2:
        score += 10

    if EMAIL_RE.match(lead.email):
        score += 25

    score += max(0, min(5, lead.budget)) * 8

    timeline = lead.timeline.lower()

    if timeline == "urgent":
        score += 20
    elif timeline == "quarter":
        score += 12
    elif timeline == "month":
        score += 8
    elif timeline == "later":
        score += 3

    score += min(20, len(lead.message.strip()) // 10)

    if lead.consent:
        score += 5

    return min(100, score)


@app.get("/")
def status():
    return {"status": "ok"}


@app.post("/api/leads")
def create_lead(lead: Lead):
    if len(lead.name.strip()) < 2:
        raise HTTPException(status_code=400, detail="Invalid name")

    if not EMAIL_RE.match(lead.email):
        raise HTTPException(status_code=400, detail="Invalid email")

    score = score_lead(lead)
    tier = "hot" if score >= 75 else "warm" if score >= 45 else "cold"

    return {"score": score, "tier": tier}
