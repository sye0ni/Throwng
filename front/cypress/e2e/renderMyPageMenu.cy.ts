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

  it("renderMyPageMenu", () => {
    cy.get('[href="/user/mypage"]').click();
    cy.get('.menu-body > :nth-child(1)').click();
    cy.get('.Header svg').click({force:true});
    cy.get('.menu-body > :nth-child(2)').click();
    cy.get('.Header svg').click({force:true});
    cy.get('.menu-body > :nth-child(3)').click();
    cy.get('.Header svg').click({force:true});
    cy.get('.drop-pick svg').click()
    cy.get('.background').click({force: true});
    cy.get('.Header svg > :nth-child(1)').eq(1).click({force: true});
    cy.get('.background').click({force: true});
  });
});
