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

  it("renderMyPlayList", () => {
    cy.get('[href="/user/playlist"]').click();
    cy.get('.body').scrollTo('bottom');
  });
});
