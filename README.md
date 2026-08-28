# Belfort Luxe Conversion Engine

A luxury, high-conversion front-end system paired with multi-language backend lead-intake scaffolding.

## Product structure

```text
belfort-luxe-conversion-engine/
  README.md
  index.html
  backend/
    python/
      app.py
      requirements.txt
    typescript/
      server.ts
      package.json
      tsconfig.json
    java/
      pom.xml
      src/
        main/
          java/
            com/
              belfortlux/
                LeadApplication.java
                LeadController.java
```

## Front-end

Open `index.html` directly in a browser.

The page includes:

- Combined HTML, CSS, and JavaScript
- Luxury conversion-focused presentation
- Client-side lead scoring
- Optional same-origin API scoring at `/api/leads`

## Python API

```bash
cd backend/python
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app:app --reload
```

## TypeScript API

```bash
cd backend/typescript
npm install
npm run dev
```

## Java API

```bash
cd backend/java
mvn spring-boot:run
```

## API contract

POST `/api/leads`

Request body:

```json
{
  "name": "Jordan Belfort",
  "company": "Straight Line",
  "email": "jordan@example.com",
  "budget": 5,
  "timeline": "urgent",
  "message": "Build a high-conversion luxury web presence.",
  "consent": true
}
```

Response body:

```json
{
  "score": 92,
  "tier": "hot"
}
```
## References

Mozilla Developer Network. (n.d.). HTML reference. MDN Web Docs. Retrieved August 28, 2026, from https://developer.mozilla.org/en-US/docs/Web/HTML

Mozilla Developer Network. (n.d.). CSS reference. MDN Web Docs. Retrieved August 28, 2026, from https://developer.mozilla.org/en-US/docs/Web/CSS

Mozilla Developer Network. (n.d.). JavaScript reference. MDN Web Docs. Retrieved August 28, 2026, from https://developer.mozilla.org/en-US/docs/Web/JavaScript

OpenJS Foundation. (n.d.). Express. Retrieved August 28, 2026, from https://expressjs.com/

Ramírez, S. (n.d.). FastAPI. Retrieved August 28, 2026, from https://fastapi.tiangolo.com/

Spring. (n.d.). Spring Boot. Retrieved August 28, 2026, from https://spring.io/projects/spring-boot
