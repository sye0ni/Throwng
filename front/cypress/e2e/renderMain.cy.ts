describe("Throwng", () => {

  beforeEach(() => {
    cy.viewport('iphone-8');
    cy.visit("http://localhost:5173");
    window.localStorage.setItem(
      "jwt",
      "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIzNDU4NzgzODI4Iiwic3ViIjoiYWNjZXNzVG9rZW4iLCJpYXQiOjE3MTQ2MzY5MjQsImV4cCI6MTcxNzIyODkyNH0.88Z91GZEPLlCzwPFfVVzD-mezasMF-tnG6gYsH2iAF8"
    );
    // const jwtToken = Cypress.env('jwtToken');
    // window.localStorage.setItem("jwt", jwtToken);
  });

  it("render Main Page and scroll", () => {
    cy.get('.active').should('have.text','홈');
    cy.get('[href="/user/playlist"]').should('have.text','플레이리스트');
    cy.get('.circle');
    cy.get('[href="/content"]').should('have.text','컨텐츠');
    cy.get('[href="/user/mypage"]').should('have.text','마이');
    cy.get('.GpsBtn').click();
    cy.get('[href="/user/playlist"]').click();
    cy.get('.Header svg').click({force:true});
    cy.get('.circle').click({force:true});
    cy.get('.Header svg').click({force:true});
    cy.get('[href="/content"]').click();
    cy.get('.Header svg').click({force:true});
    cy.get('[href="/user/mypage"]').click();
    cy.get('.Header svg > :nth-child(1)').eq(0).click({force: true});
  });
});
